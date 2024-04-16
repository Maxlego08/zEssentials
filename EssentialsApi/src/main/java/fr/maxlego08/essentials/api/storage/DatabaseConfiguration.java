package fr.maxlego08.essentials.api.storage;

/**
 * Represents the configuration for connecting to a database.
 * This record encapsulates the database connection details, including the prefix, username, password, port, host,
 * database name, and debug mode.
 */
public record DatabaseConfiguration(String prefix, String user, String password, int port, String host,
                                    String database, boolean debug) {

    /**
     * Replaces the placeholder %prefix% in the given table name with the actual prefix.
     *
     * @param tableName The table name possibly containing the %prefix% placeholder.
     * @return The table name with the %prefix% placeholder replaced by the actual prefix.
     */
    public String replacePrefix(String tableName) {
        return tableName.replaceAll("%prefix%", prefix);
    }

}
