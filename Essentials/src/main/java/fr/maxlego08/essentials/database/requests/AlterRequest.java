package fr.maxlego08.essentials.database.requests;

import fr.maxlego08.essentials.api.database.ColumnDefinition;
import fr.maxlego08.essentials.api.database.Executor;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AlterRequest implements Executor {

    private final Schema schema;

    public AlterRequest(Schema schema) {
        this.schema = schema;
    }

    @Override
    public int execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException {

        StringBuilder alterTableSQL = new StringBuilder("ALTER TABLE ");
        alterTableSQL.append(this.schema.getTableName()).append(" ");

        List<String> columnSQLs = new ArrayList<>();
        for (ColumnDefinition column : this.schema.getColumns()) {
            columnSQLs.add("ADD COLUMN " + column.build());
        }
        alterTableSQL.append(String.join(", ", columnSQLs));

        if (!this.schema.getPrimaryKeys().isEmpty()) {
            alterTableSQL.append(", PRIMARY KEY (").append(String.join(", ", this.schema.getPrimaryKeys())).append(")");
        }

        for (String fk : this.schema.getForeignKeys()) {
            alterTableSQL.append(", ADD ").append(fk);
        }

        String finalQuery = databaseConfiguration.replacePrefix(alterTableSQL.toString());
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
