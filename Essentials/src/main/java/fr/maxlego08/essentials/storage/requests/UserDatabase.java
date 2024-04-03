package fr.maxlego08.essentials.storage.requests;

import java.util.UUID;

public class UserDatabase extends Table {

    public UserDatabase(MySqlConnection connection) {
        super(connection, "players");
    }

    @Override
    protected String getCreate() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "uuid VARCHAR(36) NOT NULL," +
                "name VARCHAR(16) NOT NULL," +
                "PRIMARY KEY (uuid))";
    }

    public void upsert(UUID uuid, String name) {
        String sql = "INSERT INTO %s (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(`name`)";

        this.update(sql, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
        });
    }

}
