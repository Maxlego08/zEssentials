package fr.maxlego08.essentials.bot.storage;

import fr.maxlego08.essentials.bot.config.Configuration;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.logger.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StorageManager {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private RequestHelper requestHelper;

    public void connect(Configuration configuration) {

        DatabaseConfiguration databaseConfiguration = configuration.getDatabaseConfiguration().toDatabaseConfiguration();
        DatabaseConnection databaseConnection = new HikariDatabaseConnection(databaseConfiguration);

        if (!databaseConnection.isValid()) {
            System.err.println("Database connection failed.");
            return;
        }

        Logger logger = System.out::println;
        this.requestHelper = new RequestHelper(databaseConnection, logger);

        MigrationManager.setMigrationTableName("zessentials_migrations");
        MigrationManager.setDatabaseConfiguration(databaseConfiguration);

        // Register migrations

        MigrationManager.execute(databaseConnection, logger);
    }

    public RequestHelper getRequestHelper() {
        return requestHelper;
    }
}
