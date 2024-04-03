package fr.maxlego08.essentials.api.database;

import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.util.logging.Logger;

public interface MigrationManager {

    void registerMigration();

    void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger);
}
