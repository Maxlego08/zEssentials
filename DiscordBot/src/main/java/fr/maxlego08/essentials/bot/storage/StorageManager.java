package fr.maxlego08.essentials.bot.storage;

import fr.maxlego08.essentials.bot.config.Configuration;
import fr.maxlego08.essentials.bot.link.AccountDTO;
import fr.maxlego08.essentials.bot.link.CodeDTO;
import fr.maxlego08.essentials.bot.storage.migrations.LinkAccountMigration;
import fr.maxlego08.essentials.bot.storage.migrations.LinkCodeMigrations;
import fr.maxlego08.essentials.bot.utils.Tables;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.logger.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

        MigrationManager.registerMigration(new LinkCodeMigrations());
        MigrationManager.registerMigration(new LinkAccountMigration());

        MigrationManager.execute(databaseConnection, logger);
    }

    public RequestHelper getRequestHelper() {
        return requestHelper;
    }

    public List<CodeDTO> loadCodes() {
        return this.requestHelper.selectAll(Tables.LINK_CODES, CodeDTO.class);
    }

    public void saveCode(CodeDTO code) {
        this.executor.execute(() -> this.requestHelper.insert(Tables.LINK_CODES, table -> {
            table.object("code", code.code());
            table.object("user_id", code.user_id());
        }));
    }

    public Optional<AccountDTO> getAccount(long userId) {
        return this.requestHelper.select(Tables.LINK_ACCOUNTS, AccountDTO.class, table -> table.bigInt("user_id", userId)).stream().findFirst();
    }

    public void isAccountLinked(long userId, Consumer<Boolean> consumer) {
        executor.execute(() -> consumer.accept(getAccount(userId).isPresent()));
    }
}
