package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.requests.SqlConnection;
import fr.maxlego08.essentials.storage.requests.Repositories;
import fr.maxlego08.essentials.storage.requests.repositeries.UserCooldownsRepository;
import fr.maxlego08.essentials.storage.requests.repositeries.UserRepository;
import fr.maxlego08.essentials.storage.requests.repositeries.UserOptionRepository;
import fr.maxlego08.essentials.user.ZUser;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SqlStorage implements IStorage {

    private final SqlConnection connection;
    private final EssentialsPlugin plugin;
    private final Map<UUID, User> users = new HashMap<>();
    private final Repositories repositories;

    public SqlStorage(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.connection = new SqlConnection(plugin.getConfiguration().getDatabaseConfiguration());

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

        plugin.getMigrationManager().execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration());
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
        this.plugin.getScheduler().runAsync(wrappedTask -> this.repositories.getTable(UserOptionRepository.class).upsert(uniqueId, option, value));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        this.plugin.getScheduler().runAsync(wrappedTask -> this.repositories.getTable(UserCooldownsRepository.class).upsert(uniqueId, key, expiredAt));
    }
}
