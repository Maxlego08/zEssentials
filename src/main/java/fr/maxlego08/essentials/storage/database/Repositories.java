package fr.maxlego08.essentials.storage.database;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.HashMap;
import java.util.Map;

public class Repositories {

    private final EssentialsPlugin plugin;
    private final DatabaseConnection connection;

    private final Map<Class<? extends Repository>, Repository> tables = new HashMap<>();

    public Repositories(EssentialsPlugin plugin, DatabaseConnection connection) {
        this.plugin = plugin;
        this.connection = connection;
    }

    public void register(Class<? extends Repository> tableClass) {
        try {
            Repository repository = tableClass.getConstructor(EssentialsPlugin.class, DatabaseConnection.class).newInstance(this.plugin, this.connection);
            this.tables.put(tableClass, repository);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public <T extends Repository> T getTable(Class<T> module) {
        return (T) this.tables.get(module);
    }
}
