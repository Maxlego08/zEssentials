package fr.maxlego08.essentials.api.database;

import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;

import java.sql.Connection;
import java.util.logging.Logger;

/**
 * Represents a manager for handling database migrations.
 */
public interface MigrationManager {

    /**
     * Registers database migrations.
     */
    void registerMigration();

    /**
     * Executes database migrations.
     *
     * @param connection            The connection to the database.
     * @param databaseConfiguration The configuration for the database.
     * @param logger                The logger for logging messages.
     */
    void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger);
}
