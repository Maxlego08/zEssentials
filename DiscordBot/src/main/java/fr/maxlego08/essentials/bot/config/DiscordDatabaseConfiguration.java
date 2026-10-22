package fr.maxlego08.essentials.bot.config;

import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;

public record DiscordDatabaseConfiguration(String tablePrefix, String user, String password, int port, String host,
                                           String database, boolean debug, DatabaseType type) {

    public DiscordDatabaseConfiguration(String tablePrefix, String user, String password, int port, String host, String database, boolean debug) {
        this(tablePrefix, user, password, port, host, database, debug, DatabaseType.MYSQL);
    }

    public DatabaseConfiguration toDatabaseConfiguration() {
        DatabaseType databaseType = type == null ? DatabaseType.MYSQL : type;
        if (databaseType != DatabaseType.MYSQL && databaseType != DatabaseType.MARIADB) {
            throw new IllegalArgumentException("The Discord bot database type must be MYSQL or MARIADB.");
        }
        return new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, databaseType);
    }
}
