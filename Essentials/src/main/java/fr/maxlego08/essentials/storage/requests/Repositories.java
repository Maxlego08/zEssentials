package fr.maxlego08.essentials.storage.requests;

import java.util.HashMap;
import java.util.Map;

public class Repositories {

    private final SqlConnection connection;

    private final Map<Class<? extends Repository>, Repository> tables = new HashMap<>();

    public Repositories(SqlConnection connection) {
        this.connection = connection;
    }

    public void register(Class<? extends Repository> tableClass) {
        try {
            Repository repository = tableClass.getConstructor(SqlConnection.class).newInstance(connection);
            this.tables.put(tableClass, repository);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public <T extends Repository> T getTable(Class<T> module) {
        return (T) this.tables.get(module);
    }
}
