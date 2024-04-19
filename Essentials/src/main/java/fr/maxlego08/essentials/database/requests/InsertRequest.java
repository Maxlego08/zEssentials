package fr.maxlego08.essentials.database.requests;

import fr.maxlego08.essentials.api.database.ColumnDefinition;
import fr.maxlego08.essentials.api.database.Executor;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class InsertRequest implements Executor {

    private final Schema schema;

    public InsertRequest(Schema schema) {
        this.schema = schema;
    }

    @Override
    public int execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {

        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + this.schema.getTableName() + " (");
        StringBuilder valuesQuery = new StringBuilder("VALUES (");

        List<Object> values = new ArrayList<>();

        for (int i = 0; i < this.schema.getColumns().size(); i++) {
            ColumnDefinition columnDefinition = this.schema.getColumns().get(i);
            insertQuery.append(i > 0 ? ", " : "").append(columnDefinition.getName());
            valuesQuery.append(i > 0 ? ", " : "").append("?");
            values.add(columnDefinition.getObject());
        }

        insertQuery.append(") ");
        valuesQuery.append(")");
        String upsertQuery = databaseConfiguration.replacePrefix(insertQuery + valuesQuery.toString());

        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + upsertQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(upsertQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            } catch (Exception exception) {
                return -1;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute upsert: " + exception.getMessage(), exception);
        }
    }
}
