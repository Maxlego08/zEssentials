package fr.maxlego08.essentials.database.requests;

import fr.maxlego08.essentials.api.database.Executor;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;
import fr.maxlego08.essentials.api.database.ColumnDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CreateRequest implements Executor {

    private final Schema schema;

    public CreateRequest(Schema schema) {
        this.schema = schema;
    }

    @Override
    public int execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {

        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSQL.append(this.schema.getTableName()).append(" (");

        List<String> columnSQLs = new ArrayList<>();
        for (ColumnDefinition column : this.schema.getColumns()) {
            columnSQLs.add(column.build());
        }
        createTableSQL.append(String.join(", ", columnSQLs));

        if (!this.schema.getPrimaryKeys().isEmpty()) {
            createTableSQL.append(", PRIMARY KEY (").append(String.join(", ", this.schema.getPrimaryKeys())).append(")");
        }

        for (String fk : this.schema.getForeignKeys()) {
            createTableSQL.append(", ").append(fk);
        }

        createTableSQL.append(")");

        String finalQuery = databaseConfiguration.replacePrefix(createTableSQL.toString());
        if (databaseConfiguration.debug()) {
            logger.info("Executing SQL: " + finalQuery);
        }

        try (PreparedStatement statement = connection.prepareStatement(finalQuery)) {
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new SQLException("Failed to execute schema creation: " + exception.getMessage(), exception);
        }

        return -1;
    }
}
