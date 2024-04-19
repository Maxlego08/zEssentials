package fr.maxlego08.essentials.database.requests;

import fr.maxlego08.essentials.api.database.Executor;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DeleteRequest implements Executor {

    private final Schema schemaBuilder;

    public DeleteRequest(Schema schemaBuilder) {
        this.schemaBuilder = schemaBuilder;
    }

    @Override
    public int execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(schemaBuilder.getTableName());
        schemaBuilder.whereConditions(sql);

        String finalQuery = databaseConfiguration.replacePrefix(sql.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(finalQuery)) {
            schemaBuilder.applyWhereConditions(preparedStatement, 1);
            preparedStatement.executeUpdate();
        }
        return -1;
    }
}
