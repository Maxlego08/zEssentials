package fr.maxlego08.essentials.api.storage;

public record DatabaseConfiguration(String prefix, String user, String password, int port, String host,
                                    String database, boolean debug) {

    public String replacePrefix(String tableName) {
        return tableName.replaceAll("%prefix%", prefix);
    }

}
