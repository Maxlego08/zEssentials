package fr.maxlego08.essentials.api.database;

import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public interface Schema {
    Schema uuid(String columnName);

    Schema uuid(String columnName, UUID value);

    Schema string(String columnName, int length);

    Schema string(String columnName, String value);

    Schema bigInt(String columnName);

    Schema bigInt(String columnName, long value);

    Schema bool(String columnName);

    Schema bool(String columnName, boolean value);

    Schema primary();

    Schema foreignKey(String referenceTable);

    Schema createdAt();

    Schema updatedAt();

    Schema timestamps();

    Schema nullable();

    Schema defaultValue(String value);

    void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) throws SQLException;
}

