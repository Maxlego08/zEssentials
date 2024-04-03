package fr.maxlego08.essentials.storage.requests.repositeries;

import fr.maxlego08.essentials.storage.requests.Repository;
import fr.maxlego08.essentials.storage.requests.SqlConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCooldownsRepository extends Repository {

    public UserCooldownsRepository(SqlConnection connection) {
        super(connection, "player_cooldowns");
    }

    public void upsert(UUID uuid, String cooldownName, long cooldownValue) {
        String sql = "INSERT INTO %s (unique_id, cooldown_name, cooldown_value) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE cooldown_value = VALUES(cooldown_value)";

        this.update(sql, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, cooldownName);
            preparedStatement.setLong(3, cooldownValue);
        });
    }

    public Map<String, Long> selectCooldowns(UUID uuid) {
        Map<String, Long> cooldowns = new HashMap<>();
        String sql = "SELECT cooldown_name, cooldown_value FROM %s WHERE unique_id = ?";

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
