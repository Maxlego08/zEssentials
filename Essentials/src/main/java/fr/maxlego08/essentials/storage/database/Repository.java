package fr.maxlego08.essentials.storage.database;

import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.database.SchemaBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Repository {

    private final SqlConnection connection;
    private final String tableName;

    public Repository(SqlConnection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected Connection getConnection() {
        return this.connection.getConnection();
    }

    public String getTableName() {
        return this.connection.getDatabaseConfiguration().prefix() + tableName;
    }

    protected void upsert(Consumer<Schema> consumer) {
        try {
            SchemaBuilder.upsert(getTableName(), consumer).execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected <T> List<T> select(Class<T> clazz, Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.select(getTableName());
        consumer.accept(schema);
        try {
            return schema.executeSelect(clazz, this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected void delete(Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.delete(getTableName());
        consumer.accept(schema);
        try {
            schema.execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}