package fr.maxlego08.essentials.storage.requests;

import fr.maxlego08.essentials.api.functionnals.ResultSetConsumer;
import fr.maxlego08.essentials.api.functionnals.StatementConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Table {

    private final MySqlConnection connection;
    private final String tableName;

    public Table(MySqlConnection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected abstract String getCreate();

    public void create() {
        try (PreparedStatement statement = this.connection.getConnection().prepareStatement(String.format(this.getCreate(), getTableName(), this.connection.getDatabaseConfiguration().prefix() + "players"))) {
            statement.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected Connection getConnection() {
        return this.connection.getConnection();
    }

    public String getTableName() {
        return this.connection.getDatabaseConfiguration().prefix() + tableName;
    }

    protected void update(String request, StatementConsumer consumer) {
        try (PreparedStatement statement = getConnection().prepareStatement(String.format(request, getTableName()))) {
            consumer.accept(statement);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void query(String request, StatementConsumer statementConsumer, ResultSetConsumer resultSetConsumer) {
        try (PreparedStatement preparedStatement = this.connection.getConnection().prepareStatement(String.format(request, getTableName()))) {

            statementConsumer.accept(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSetConsumer.accept(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}