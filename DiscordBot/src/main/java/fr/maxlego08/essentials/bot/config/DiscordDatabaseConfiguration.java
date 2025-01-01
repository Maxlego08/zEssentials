package fr.maxlego08.essentials.bot.config;

import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;

public record DiscordDatabaseConfiguration(String tablePrefix, String user, String password, int port, String host,
                                           String database, boolean debug) {

    public DatabaseConfiguration toDatabaseConfiguration() {
        return new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, DatabaseType.MYSQL);
    }
}
