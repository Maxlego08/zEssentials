package fr.maxlego08.essentials.storage.requests;

import fr.maxlego08.essentials.api.functionnals.StatementConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class Table {

    private final MySqlConnection connection;
    private final String tableName;

    public Table(MySqlConnection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected abstract String getCreate();

    public void create() {
        try (PreparedStatement statement = connection.getConnection().prepareStatement(String.format(this.getCreate(), getTableName()))) {
            statement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected Connection getConnection() {
        return connection.getConnection();
    }

    public String getTableName() {
        return connection.getDatabaseConfiguration().prefix() + tableName;
    }

    protected void update(String request, StatementConsumer consumer) {
        try (PreparedStatement statement = getConnection().prepareStatement(String.format(request, getTableName()))) {
            consumer.accept(statement);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}