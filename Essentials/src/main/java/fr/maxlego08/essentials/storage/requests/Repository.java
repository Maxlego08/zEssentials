package fr.maxlego08.essentials.storage.requests;

import fr.maxlego08.essentials.api.functionnals.ResultSetConsumer;
import fr.maxlego08.essentials.api.functionnals.StatementConsumer;
import fr.maxlego08.essentials.database.SchemaBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    protected void update(String request, StatementConsumer consumer) {
        try (PreparedStatement statement = getConnection().prepareStatement(String.format(request, getTableName()))) {
            consumer.accept(statement);
            statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void upsert(Consumer<SchemaBuilder> consumer) {
        try {
            SchemaBuilder.upsert(getTableName(), consumer).execute(this.connection.getConnection(), this.connection.getDatabaseConfiguration(), this.connection.getPlugin().getLogger());
        } catch (SQLException exception) {
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