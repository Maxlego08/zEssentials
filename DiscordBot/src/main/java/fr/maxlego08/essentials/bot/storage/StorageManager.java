package fr.maxlego08.essentials.bot.storage;

import fr.maxlego08.essentials.api.discord.DiscordAction;
import fr.maxlego08.essentials.api.dto.DiscordAccountDTO;
import fr.maxlego08.essentials.api.dto.DiscordCodeDTO;
import fr.maxlego08.essentials.bot.config.Configuration;
import fr.maxlego08.essentials.bot.utils.Tables;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.logger.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class StorageManager {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private RequestHelper requestHelper;

    public void connect(Configuration configuration) {

        DatabaseConfiguration databaseConfiguration = configuration.getDatabaseConfiguration().toDatabaseConfiguration();
        Logger logger = System.out::println;
        DatabaseConnection databaseConnection = new HikariDatabaseConnection(databaseConfiguration, logger);

        if (!databaseConnection.isValid()) {
            System.err.println("Database connection failed.");
            return;
        }

        checkDatabaseVersion(databaseConnection);
        this.requestHelper = new RequestHelper(databaseConnection, logger);
    }

    private void checkDatabaseVersion(DatabaseConnection databaseConnection) {
        try (Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            String version = metadata.getDatabaseProductVersion();
            if (!"MySQL".equalsIgnoreCase(metadata.getDatabaseProductName()) || version.toLowerCase(Locale.ROOT).contains("mariadb")) {
                return;
            }
            int major = metadata.getDatabaseMajorVersion();
            int minor = metadata.getDatabaseMinorVersion();
            if (major < 8 || (major == 8 && minor < 4)) {
                System.err.println("WARNING: Detected MySQL " + version + ". MySQL Connector/J 26.10.0 supports MySQL 8.4 or newer. Please upgrade your database server.");
            }
        } catch (Exception exception) {
            System.err.println("WARNING: Unable to check the database server version: " + exception.getMessage());
        }
    }

    public RequestHelper getRequestHelper() {
        return requestHelper;
    }

    public void saveCode(DiscordCodeDTO code) {
        this.executor.execute(() -> this.requestHelper.insert(Tables.LINK_CODES, table -> {
            table.object("code", code.code());
            table.object("user_id", code.user_id());
            table.object("discord_name", code.discord_name());
        }));
    }

    public Optional<DiscordAccountDTO> getAccount(long userId) {
        return this.requestHelper.select(Tables.LINK_ACCOUNTS, DiscordAccountDTO.class, table -> table.where("user_id", userId)).stream().findFirst();
    }

    public void isAccountLinked(long userId, Consumer<Boolean> consumer) {
        executor.execute(() -> consumer.accept(getAccount(userId).isPresent()));
    }

    public void insertLog(DiscordAction action, UUID uniqueId, String minecraftName, String discordName, long userId, String data) {
        this.executor.execute(() -> this.requestHelper.insert(Tables.LINK_HISTORIES, table -> {
            table.string("action", action.name());
            if (uniqueId != null) table.uuid("minecraft_id", uniqueId);
            if (minecraftName != null) table.string("minecraft_name", minecraftName);
            if (discordName != null) table.string("discord_name", discordName);
            if (userId != -1) table.bigInt("discord_id", userId);
            if (data != null) table.object("data", data);
        }));
    }

    public Optional<DiscordCodeDTO> getCode(long userId) {
        return this.requestHelper.select(Tables.LINK_CODES, DiscordCodeDTO.class, table -> table.where("user_id", userId)).stream().findFirst();
    }
}
