package fr.maxlego08.essentials.storage.requests.repositeries;

import fr.maxlego08.essentials.storage.requests.Repository;
import fr.maxlego08.essentials.storage.requests.SqlConnection;

import java.util.UUID;

public class UserRepository extends Repository {

    public UserRepository(SqlConnection connection) {
        super(connection, "players");
    }

    public void upsert(UUID uuid, String name) {
        String sql = "INSERT INTO %s (unique_id, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(`name`)";

        this.update(sql, preparedStatement -> {
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
        });
    }

}
