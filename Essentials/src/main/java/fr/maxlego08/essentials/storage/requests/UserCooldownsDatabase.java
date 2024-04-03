package fr.maxlego08.essentials.storage.requests;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCooldownsDatabase extends Table {

    public UserCooldownsDatabase(MySqlConnection connection) {
        super(connection, "player_cooldowns");
    }

    @Override
    protected String getCreate() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "uuid VARCHAR(36) NOT NULL," +
                "cooldown_name VARCHAR(255) NOT NULL," +
                "cooldown_value BIGINT NOT NULL," +
                "PRIMARY KEY (uuid, cooldown_name)," +
                "FOREIGN KEY (uuid) REFERENCES %s(uuid) ON DELETE CASCADE)";
    }

    public void upsert(UUID uuid, String cooldownName, long cooldownValue) {
        String sql = "INSERT INTO %s (uuid, cooldown_name, cooldown_value) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cooldown_value = VALUES(cooldown_value)";

        this.update(sql, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, cooldownName);
            preparedStatement.setLong(3, cooldownValue);
        });
    }

    public Map<String, Long> selectCooldowns(UUID uuid) {
        Map<String, Long> cooldowns = new HashMap<>();
        String sql = "SELECT cooldown_name, cooldown_value FROM %s WHERE uuid = ?";

        this.query(sql, preparedStatement -> preparedStatement.setString(1, uuid.toString()), resultSet -> {
            while (resultSet.next()) {
                cooldowns.put(resultSet.getString("cooldown_name"), resultSet.getLong("cooldown_value"));
            }
        });

        return cooldowns;
    }

    public void deleteExpiredCooldowns() {
        String sql = "DELETE FROM %s WHERE cooldown_value < ?";
        long currentTime = System.currentTimeMillis();
        this.update(sql, preparedStatement -> preparedStatement.setLong(1, currentTime));
    }
}
