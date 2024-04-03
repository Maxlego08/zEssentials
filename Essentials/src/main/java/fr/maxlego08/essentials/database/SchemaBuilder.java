package fr.maxlego08.essentials.database;

import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class SchemaBuilder implements Schema {
    private final String tableName;
    private final List<ColumnDefinition> columns = new ArrayList<>();
    private final List<String> primaryKeys = new ArrayList<>();
    private final List<String> foreignKeys = new ArrayList<>();

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
        return addColumn(new ColumnDefinition(columnName, "VARCHAR").setLength(length));
    }

    @Override
    public Schema bigInt(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "BIGINT"));
    }

    @Override
    public Schema bool(String columnName) {
        return addColumn(new ColumnDefinition(columnName, "BOOLEAN"));
    }

    @Override
    public Schema foreignKey(String referenceTable) {
        if (this.columns.isEmpty()) throw new IllegalStateException("No column defined to apply foreign key.");
        ColumnDefinition lastColumn = this.columns.get(this.columns.size() - 1);

        String fkDefinition = String.format("FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE", lastColumn.getName(), referenceTable, lastColumn.getName());
        this.foreignKeys.add(fkDefinition);
        return this;
    }


    @Override
    public Schema createdAt() {
        ColumnDefinition column = new ColumnDefinition("created_at", "TIMESTAMP");
        column.setDefaultValue("CURRENT_TIMESTAMP");
        this.columns.add(column);
        return this;
    }

    @Override
    public Schema updatedAt() {
        ColumnDefinition column = new ColumnDefinition("updated_at", "TIMESTAMP");
        column.setDefaultValue("CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        this.columns.add(column);
        return this;
    }

    @Override
    public Schema nullable() {
        getLastColumn().setNullable(true);
        return this;
    }

    @Override
    public Schema defaultValue(String value) {
        getLastColumn().setDefaultValue(value);
        return this;
    }

    @Override
    public Schema primary() {
        ColumnDefinition lastColumn = getLastColumn();
        lastColumn.setPrimaryKey(true);
        primaryKeys.add(lastColumn.getName());
        return this;
    }

    private Schema addColumn(ColumnDefinition column) {
        columns.add(column);
        return this;
    }

    @Override
    public Schema timestamps() {
        this.createdAt();
        this.updatedAt();
        return this;
    }

    @Override
    public void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSQL.append(this.tableName).append(" (");

        List<String> columnSQLs = new ArrayList<>();
        for (ColumnDefinition column : this.columns) {
            columnSQLs.add(column.build());
        }
        createTableSQL.append(String.join(", ", columnSQLs));

        if (!this.primaryKeys.isEmpty()) {
            createTableSQL.append(", PRIMARY KEY (").append(String.join(", ", this.primaryKeys)).append(")");
        }

        for (String fk : this.foreignKeys) {
            createTableSQL.append(", ").append(fk);
        }

        createTableSQL.append(")");

        String finalSQL = databaseConfiguration.replacePrefix(createTableSQL.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalSQL);
        }

        try (PreparedStatement statement = connection.prepareStatement(finalSQL)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to execute schema creation: " + e.getMessage(), e);
        }
    }

    private ColumnDefinition getLastColumn() {
        if (columns.isEmpty()) throw new IllegalStateException("No columns defined.");
        return columns.get(columns.size() - 1);
    }
}
