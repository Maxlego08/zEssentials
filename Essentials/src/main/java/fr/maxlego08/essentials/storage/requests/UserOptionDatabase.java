package fr.maxlego08.essentials.storage.requests;

import fr.maxlego08.essentials.api.user.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserOptionDatabase extends Table {

    public UserOptionDatabase(MySqlConnection connection) {
        super(connection, "player_options");
    }

    @Override
    protected String getCreate() {
        return "CREATE TABLE IF NOT EXISTS %s (" +
                "uuid VARCHAR(36) NOT NULL," +
                "option_name VARCHAR(255) NOT NULL," +
                "option_value BOOLEAN NOT NULL," +
                "PRIMARY KEY (uuid, option_name)," +
                "FOREIGN KEY (uuid) REFERENCES %s(uuid) ON DELETE CASCADE)";
    }

    public void upsert(UUID uuid, Option option, boolean optionValue) {
        String sql = "INSERT INTO %s (uuid, option_name, option_value) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE option_value = VALUES(option_value)";

        this.update(sql, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, option.name());
            preparedStatement.setBoolean(3, optionValue);
        });
    }

    public Map<Option, Boolean> selectOptions(UUID uuid) {
        Map<Option, Boolean> options = new HashMap<>();
        String sql = "SELECT option_name, option_value FROM %s WHERE uuid = ?";

        this.query(sql, preparedStatement -> preparedStatement.setString(1, uuid.toString()), resultSet -> {
            while (resultSet.next()) {
                options.put(Option.valueOf(resultSet.getString("option_name")), resultSet.getBoolean("option_value"));
            }
        });

        return options;
    }
}
