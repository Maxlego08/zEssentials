package fr.maxlego08.essentials.database;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.api.database.MigrationManager;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;
import fr.maxlego08.essentials.database.migrations.CreateEconomyTransactionMigration;
import fr.maxlego08.essentials.database.migrations.CreateServerStorageTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserCooldownTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserEconomyMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserOptionTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserTableMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ZMigrationManager implements MigrationManager {

    private static final List<Schema> schemas = new ArrayList<>();
    private final EssentialsPlugin plugin;
    private final List<Migration> migrations = new ArrayList<>();

    public ZMigrationManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public static void registerSchema(Schema schema) {
        schemas.add(schema);
    }

    @Override
    public void registerMigration() {
        this.migrations.add(new CreateUserTableMigration());
        this.migrations.add(new CreateUserOptionTableMigration());
        this.migrations.add(new CreateUserCooldownTableMigration());
        this.migrations.add(new CreateUserEconomyMigration());
        this.migrations.add(new CreateEconomyTransactionMigration());
        this.migrations.add(new CreateServerStorageTableMigration());
    }

    @Override
    public void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) {
        this.migrations.forEach(migration -> {
            migration.setPrefix(databaseConfiguration.prefix());
            migration.up();
        });
        schemas.forEach(schema -> {
            try {
                schema.execute(connection, databaseConfiguration, logger);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}
