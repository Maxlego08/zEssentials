package fr.maxlego08.essentials.storage.storages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.requests.MySqlConnection;
import fr.maxlego08.essentials.storage.requests.UserDatabase;
import fr.maxlego08.essentials.user.ZUser;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SqlStorage implements IStorage {

    private final MySqlConnection connection;
    private final EssentialsPlugin plugin;
    private final Map<UUID, User> users = new HashMap<>();
    private final UserDatabase userDatabase;

    public SqlStorage(EssentialsPlugin plugin) {
        this.plugin = plugin;
        this.connection = new MySqlConnection(plugin.getConfiguration().getDatabaseConfiguration());

        if (!this.connection.isValid()) {
            plugin.getLogger().severe("Unable to connect to database !");
            Bukkit.getPluginManager().disablePlugin(plugin);
        } else {
            plugin.getLogger().info("The database connection is valid ! (" + connection.getDatabaseConfiguration().host() + ")");
        }

        this.userDatabase = new UserDatabase(connection);

        this.userDatabase.create();
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

        plugin.getScheduler().runAsync(wrappedTask -> this.userDatabase.upsert(uniqueId, playerName));
        User user = new ZUser(this.plugin, uniqueId);
        user.setName(playerName);

        this.users.put(uniqueId, user);

        return user;
    }

    @Override
    public void onPlayerQuit(UUID uniqueId) {

        // ToDo

    }

    @Override
    public User getUser(UUID uniqueId) {
        return this.users.get(uniqueId);
    }
}
