package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.database.Repositories;
import fr.maxlego08.essentials.storage.database.SqlConnection;
import fr.maxlego08.essentials.storage.database.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class SqlStorage extends StorageHelper implements IStorage {

    private final SqlConnection connection;
    private final Repositories repositories;

    public SqlStorage(EssentialsPlugin plugin) {
        super(plugin);
        this.connection = new SqlConnection(plugin);

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().host() + ")");
        }

        this.repositories = new Repositories(this.connection);
        this.repositories.register(UserRepository.class);
        this.repositories.register(UserOptionRepository.class);
        this.repositories.register(UserCooldownsRepository.class);
        this.repositories.register(UserEconomyRepository.class);

        plugin.getMigrationManager().execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.plugin.getLogger());
        this.repositories.getTable(UserCooldownsRepository.class).deleteExpiredCooldowns();
    }

    @Override
    public void onEnable() {

        this.connection.connect();

    }

    @Override
    public void onDisable() {

        this.connection.disconnect();
    }

    @Override
    public User createOrLoad(UUID uniqueId, String playerName) {

        User user = new ZUser(this.plugin, uniqueId);
        user.setName(playerName);
        this.users.put(uniqueId, user);

        this.plugin.getScheduler().runAsync(wrappedTask -> {
            this.repositories.getTable(UserRepository.class).upsert(uniqueId, playerName);
            user.setOptions(this.repositories.getTable(UserOptionRepository.class).selectOptions(uniqueId));
            user.setCooldowns(this.repositories.getTable(UserCooldownsRepository.class).selectCooldowns(uniqueId));
            user.setEconomies(this.repositories.getTable(UserEconomyRepository.class).selectOptions(uniqueId));
        });

        return user;
    }

    @Override
    public void onPlayerQuit(UUID uniqueId) {
        this.users.remove(uniqueId);
    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.get(uniqueId);
    }

    @Override
    public void updateOption(UUID uniqueId, Option option, boolean value) {
        async(() -> this.repositories.getTable(UserOptionRepository.class).upsert(uniqueId, option, value));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        async(() -> this.repositories.getTable(UserCooldownsRepository.class).upsert(uniqueId, key, expiredAt));
    }

    @Override
    public void updateEconomy(UUID uniqueId, Economy economy, BigDecimal bigDecimal) {
        async(() -> this.repositories.getTable(UserEconomyRepository.class).upsert(uniqueId, economy, bigDecimal));
    }

    @Override
    public void updateUserMoney(UUID uniqueId, Consumer<User> consumer) {
        User fakeUser = new ZUser(this.plugin, uniqueId);
        consumer.accept(fakeUser);
    }

    @Override
    public void getUserEconomy(String userName, Consumer<List<EconomyDTO>> consumer) {
        async(() -> {
            List<EconomyDTO> economyDTOS = getLocalEconomyDTO(userName);
            if (!economyDTOS.isEmpty()) {
                consumer.accept(economyDTOS);
                return;
            }

            List<UserDTO> userDTOS = this.repositories.getTable(UserRepository.class).selectOptions(userName);
            if (userDTOS.isEmpty()) {
                consumer.accept(new ArrayList<>());
                return;
            }

            UserDTO userDTO = userDTOS.get(0);
            consumer.accept(this.repositories.getTable(UserEconomyRepository.class).selectOptions(userDTO.unique_id()));
        });
    }
}
