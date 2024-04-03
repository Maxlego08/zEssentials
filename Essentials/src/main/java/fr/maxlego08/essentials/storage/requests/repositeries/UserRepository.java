package fr.maxlego08.essentials.storage.requests.repositeries;

import fr.maxlego08.essentials.storage.requests.Repository;
import fr.maxlego08.essentials.storage.requests.SqlConnection;

import java.util.UUID;

public class UserRepository extends Repository {

    public UserRepository(SqlConnection connection) {
        super(connection, "players");
    }

    public void upsert(UUID uuid, String name) {
        upsert(table -> {
            table.uuid("unique_id", uuid);
            table.string("name", name);
        });
    }

}
