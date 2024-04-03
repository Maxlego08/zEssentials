package fr.maxlego08.essentials.api.database;

import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public interface Schema {
    Schema uuid(String columnName);

    Schema string(String columnName, int length);

    Schema bigInt(String columnName);

    Schema bool(String columnName);

    Schema primary();

    Schema foreignKey(String referenceTable);

    Schema createdAt();

    Schema updatedAt();

    Schema timestamps();

    void execute(Connection connection, DatabaseConfiguration databaseConfiguration) throws SQLException;
}

