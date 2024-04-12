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
import fr.maxlego08.essentials.storage.database.repositeries.EconomyTransactionsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserEconomyRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.storage.database.repositeries.UserRepository;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.StorageHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
        this.repositories.register(EconomyTransactionsRepository.class);

        plugin.getMigrationManager().execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.plugin.getLogger());
        this.repositories.getTable(UserCooldownsRepository.class).deleteExpiredCooldowns();
    }

    @Override
    public void onEnable() {

        this.connection.connect();
        this.totalUser = this.repositories.getTable(UserRepository.class).totalUsers();
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

            // First join !
            boolean alreadyExist = this.repositories.getTable(UserRepository.class).userAlreadyExist(uniqueId);
            if (!alreadyExist) {
                this.firstJoin(user);
            }

            this.repositories.getTable(UserRepository.class).upsert(uniqueId, playerName);
            if (alreadyExist) {
                user.setOptions(this.repositories.getTable(UserOptionRepository.class).selectOptions(uniqueId));
                user.setCooldowns(this.repositories.getTable(UserCooldownsRepository.class).selectCooldowns(uniqueId));
                user.setEconomies(this.repositories.getTable(UserEconomyRepository.class).selectEconomies(uniqueId));
            }
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

            fetchUniqueId(userName, uuid -> {
                if (uuid == null) {
                    consumer.accept(new ArrayList<>());
                    return;
                }

                consumer.accept(this.repositories.getTable(UserEconomyRepository.class).selectEconomies(uuid));
            });
        });
    }

    @Override
    public void fetchUniqueId(String userName, Consumer<UUID> consumer) {

        if (this.localUUIDS.containsKey(userName)) {
            consumer.accept(this.localUUIDS.get(userName));
            return;
        }

        async(() -> {
            // User plugin cache first
            getLocalUniqueId(userName).ifPresentOrElse(uuid -> {
                this.localUUIDS.put(userName, uuid);
                consumer.accept(uuid);
            }, () -> {

                // User server cache
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(userName);
                if (offlinePlayer != null) {
                    this.localUUIDS.put(userName, offlinePlayer.getUniqueId());
                    consumer.accept(offlinePlayer.getUniqueId());
                    return;
                }

                // Get uuid from database
                List<UserDTO> userDTOS = this.repositories.getTable(UserRepository.class).selectOptions(userName);
                if (userDTOS.isEmpty()) {
                    consumer.accept(null);
                    return;
                }

                UserDTO userDTO = userDTOS.get(0);
                this.localUUIDS.put(userName, userDTO.unique_id());
                consumer.accept(userDTO.unique_id());
            });
        });
    }

    @Override
    public void storeTransactions(UUID fromUuid, UUID toUuid, Economy economy, BigDecimal fromAmount, BigDecimal toAmount) {
        async(() -> this.repositories.getTable(EconomyTransactionsRepository.class).upsert(fromUuid, toUuid, economy, fromAmount, toAmount));
    }
}
