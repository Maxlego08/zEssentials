package fr.maxlego08.essentials.storage.database;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Schema;
import fr.maxlego08.sarah.logger.JULogger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Repository extends ZUtils {

    protected final EssentialsPlugin plugin;
    protected final DatabaseConnection connection;
    private final String tableName;

    public Repository(EssentialsPlugin plugin, DatabaseConnection connection, String tableName) {
        this.plugin = plugin;
        this.connection = connection;
        this.tableName = tableName;
    }

    protected Connection getConnection() {
        return this.connection.getConnection();
    }

    public String getTableName() {
        return this.connection.getDatabaseConfiguration().getTablePrefix() + tableName;
    }

    protected void upsert(Consumer<Schema> consumer) {
        try {
            SchemaBuilder.upsert(getTableName(), consumer).execute(this.connection, JULogger.from(this.plugin.getLogger()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected void update(Consumer<Schema> consumer) {
        try {
            SchemaBuilder.update(getTableName(), consumer).execute(this.connection, JULogger.from(this.plugin.getLogger()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected void insert(Consumer<Schema> consumer) {
        insert(consumer, id -> {
        });
    }

    protected void insert(Consumer<Schema> consumer, Consumer<Integer> consumerResult) {
        try {
            consumerResult.accept(SchemaBuilder.insert(getTableName(), consumer).execute(this.connection, JULogger.from(this.plugin.getLogger())));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    protected long select(Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.selectCount(getTableName());
        consumer.accept(schema);
        try {
            return schema.executeSelectCount(this.connection, JULogger.from(this.plugin.getLogger()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0L;
    }

    protected <T> List<T> select(Class<T> clazz, Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.select(getTableName());
        consumer.accept(schema);
        try {
            return schema.executeSelect(clazz, this.connection, JULogger.from(this.plugin.getLogger()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected void delete(Consumer<Schema> consumer) {
        Schema schema = SchemaBuilder.delete(getTableName());
        consumer.accept(schema);
        try {
            schema.execute(this.connection, JULogger.from(this.plugin.getLogger()));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}