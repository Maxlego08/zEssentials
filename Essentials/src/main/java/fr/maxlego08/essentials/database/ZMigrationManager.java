package fr.maxlego08.essentials.database;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.Migration;
import fr.maxlego08.essentials.api.database.MigrationManager;
import fr.maxlego08.essentials.api.database.Schema;
import fr.maxlego08.essentials.api.storage.DatabaseConfiguration;
import fr.maxlego08.essentials.database.migrations.CreateChatMessageMigration;
import fr.maxlego08.essentials.database.migrations.CreateEconomyTransactionMigration;
import fr.maxlego08.essentials.database.migrations.CreateSanctionsTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserCooldownTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserEconomyMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserHomeTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserOptionTableMigration;
import fr.maxlego08.essentials.database.migrations.CreateUserTableMigration;
import fr.maxlego08.essentials.database.migrations.UpdateUserTableAddSanctionColumns;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ZMigrationManager implements MigrationManager {

    private static final List<Schema> schemas = new ArrayList<>();
    private final String migrationTableName = "zessentials_migrations";
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
        // this.migrations.add(new CreateServerStorageTableMigration());
        this.migrations.add(new CreateUserHomeTableMigration());
        this.migrations.add(new CreateSanctionsTableMigration());
        this.migrations.add(new UpdateUserTableAddSanctionColumns());
        this.migrations.add(new CreateChatMessageMigration());
    }

    @Override
    public void execute(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) {

        this.createMigrationTable(connection, databaseConfiguration, logger);

        List<String> migrations = getMigrations(connection, databaseConfiguration, logger);

        this.migrations.stream().filter(migration -> !migrations.contains(migration.getClass().getSimpleName())).forEach(migration -> {
            migration.setPrefix(databaseConfiguration.prefix());
            migration.up();
        });

        schemas.forEach(schema -> {
            try {
                schema.execute(connection, databaseConfiguration, logger);
                this.insertMigration(connection, databaseConfiguration, logger, schema.getMigration());
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void createMigrationTable(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) {
        Schema schema = SchemaBuilder.create(null, this.migrationTableName, sc -> {
            sc.string("migration", 255);
            sc.createdAt();
        });
        try {
            schema.execute(connection, databaseConfiguration, logger);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private List<String> getMigrations(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger) {
        Schema schema = SchemaBuilder.select(this.migrationTableName);
        try {
            return schema.executeSelect(MigrationTable.class, connection, databaseConfiguration, logger).stream().map(MigrationTable::migration).toList();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void insertMigration(Connection connection, DatabaseConfiguration databaseConfiguration, Logger logger, Migration migration) {
        try {
            SchemaBuilder.insert(this.migrationTableName, schema -> {
                schema.string("migration", migration.getClass().getSimpleName());
            }).execute(connection, databaseConfiguration, logger);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public record MigrationTable(String migration) {

    }

}
