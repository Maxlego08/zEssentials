package fr.maxlego08.essentials.storage.requests;

import java.util.HashMap;
import java.util.Map;

public class Tables {

    private final MySqlConnection connection;

    private final Map<Class<? extends Table>, Table> tables = new HashMap<>();

    public Tables(MySqlConnection connection) {
        this.connection = connection;
    }

    public void register(Class<? extends Table> tableClass) {
        try {
            Table table = tableClass.getConstructor(MySqlConnection.class).newInstance(connection);
            this.tables.put(tableClass, table);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void create() {
        this.tables.values().forEach(Table::create);
    }

    public <T extends Table> T getTable(Class<T> module) {
        return (T) this.tables.get(module);
    }
}
