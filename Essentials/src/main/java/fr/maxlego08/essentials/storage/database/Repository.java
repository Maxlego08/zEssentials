package fr.maxlego08.essentials.storage.database;

import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.database.SchemaBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Repository extends ZUtils {

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

    protected void insert(Consumer<Schema> consumer) {
        try {
            SchemaBuilder.insert(getTableName(), consumer).execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected long select(Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.selectCount(getTableName());
        consumer.accept(schema);
        try {
            return schema.executeSelectCount(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0L;
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