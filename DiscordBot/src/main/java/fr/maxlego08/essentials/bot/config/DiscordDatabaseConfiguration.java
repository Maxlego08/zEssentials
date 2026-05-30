package fr.maxlego08.essentials.bot.config;

import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.database.DatabaseType;

import java.util.Locale;

public record DiscordDatabaseConfiguration(String tablePrefix, String user, String password, int port, String host,
                                           String database, boolean debug, String type) {

    public DatabaseConfiguration toDatabaseConfiguration() {
        return new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, getDatabaseType());
    }

    private DatabaseType getDatabaseType() {
        String value = type == null || type.isBlank() ? "MYSQL" : type;
        return switch (value.toUpperCase(Locale.ROOT)) {
            case "MARIADB" -> DatabaseType.MARIADB;
            case "POSTGRESQL" -> DatabaseType.POSTGRESQL;
            default -> DatabaseType.MYSQL;
        };
    }
}
