package fr.maxlego08.essentials.api.database;

import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;

public interface MigrationManager {

    void registerMigration();

    void execute(Connection connection, DatabaseConfiguration databaseConfiguration);
}
