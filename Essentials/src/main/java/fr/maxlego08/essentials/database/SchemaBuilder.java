package fr.maxlego08.essentials.database;

import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SchemaBuilder implements Schema {
    private final String tableName;
    private final List<String> columnDefinitions = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();
    private final List<String> primaryKeys = new ArrayList<>();
    private String lastColumnName;

    public SchemaBuilder(String tableName) {
        this.tableName = tableName;
    }

    public static void create(String tableName, Consumer<Schema> consumer) {
        Schema schema = new SchemaBuilder(tableName);
        ZMigrationManager.registerSchema(schema);
        consumer.accept(schema);
    }

    @Override
    public Schema uuid(String columnName) {
        this.string(columnName, 36);
        return this;
    }

    @Override
    public Schema string(String columnName, int length) {
        columnDefinitions.add(columnName + " VARCHAR(" + length + ")");
        lastColumnName = columnName;
        return this;
    }

    @Override
    public Schema bigInt(String columnName) {
        columnDefinitions.add(columnName + " BIGINT");
        lastColumnName = columnName;
        return this;
    }

    @Override
    public Schema bool(String columnName) {
        columnDefinitions.add(columnName + " BOOLEAN");
        lastColumnName = columnName;
        return this;
    }

    @Override
    public Schema primary() {
        if (lastColumnName == null) throw new IllegalStateException("No column defined for primary key.");
        this.primaryKeys.add(lastColumnName);
        return this;
    }

    @Override
    public Schema foreignKey(String referenceTable) {
        if (this.lastColumnName == null) throw new IllegalStateException("No column defined for foreign key.");
        String foreignKey = "FOREIGN KEY (" + this.lastColumnName + ") REFERENCES " + referenceTable + "(" + this.lastColumnName + ") ON DELETE CASCADE";
        this.foreignKeys.add(foreignKey);
        return this;
    }

    @Override
    public Schema createdAt() {
        this.columnDefinitions.add("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        this.lastColumnName = "created_at";
        return this;
    }

    @Override
    public Schema updatedAt() {
        columnDefinitions.add("updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        this.lastColumnName = "updated_at";
        return this;
    }

    @Override
    public Schema timestamps() {
        this.createdAt();
        this.updatedAt();
        return this;
    }

    @Override
    public void execute(Connection connection, DatabaseConfiguration databaseConfiguration) throws SQLException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + this.tableName + " (");
        sql.append(String.join(", ", this.columnDefinitions));

        if (!this.primaryKeys.isEmpty()) {
            sql.append(", PRIMARY KEY (").append(String.join(", ", this.primaryKeys)).append(")");
        }

        if (!this.foreignKeys.isEmpty()) {
            for (String foreignKey : this.foreignKeys) {
                sql.append(", ").append(foreignKey);
            }
        }
        sql.append(")");

        String sqlRequest = databaseConfiguration.replacePrefix(sql.toString());

        System.out.println("OUI ?");
        System.out.println(sql);
        System.out.println(sqlRequest);
        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.execute();
        }
    }
}
