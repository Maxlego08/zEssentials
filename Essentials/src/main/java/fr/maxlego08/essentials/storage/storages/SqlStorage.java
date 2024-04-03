package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.requests.MySqlConnection;
import fr.maxlego08.essentials.storage.requests.Tables;
import fr.maxlego08.essentials.storage.requests.UserCooldownsDatabase;
import fr.maxlego08.essentials.storage.requests.UserDatabase;
import fr.maxlego08.essentials.storage.requests.UserOptionDatabase;
import fr.maxlego08.essentials.user.ZUser;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SqlStorage implements IStorage {

    private final MySqlConnection connection;
    private final EssentialsPlugin plugin;
    private final Map<UUID, User> users = new HashMap<>();
    private final Tables tables;

    public SqlStorage(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.connection = new MySqlConnection(plugin.getConfiguration().getDatabaseConfiguration());

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().host() + ")");
        }

        this.tables = new Tables(this.connection);
        this.tables.register(UserDatabase.class);
        this.tables.register(UserOptionDatabase.class);
        this.tables.register(UserCooldownsDatabase.class);

        this.tables.create();
        this.tables.getTable(UserCooldownsDatabase.class).deleteExpiredCooldowns();
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
            this.tables.getTable(UserDatabase.class).upsert(uniqueId, playerName);
            user.setOptions(this.tables.getTable(UserOptionDatabase.class).selectOptions(uniqueId));
            user.setCooldowns(this.tables.getTable(UserCooldownsDatabase.class).selectCooldowns(uniqueId));
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
        this.plugin.getScheduler().runAsync(wrappedTask -> this.tables.getTable(UserOptionDatabase.class).upsert(uniqueId, option, value));
    }

    @Override
    public void updateCooldown(UUID uniqueId, String key, long expiredAt) {
        this.plugin.getScheduler().runAsync(wrappedTask -> this.tables.getTable(UserCooldownsDatabase.class).upsert(uniqueId, key, expiredAt));
    }
}
