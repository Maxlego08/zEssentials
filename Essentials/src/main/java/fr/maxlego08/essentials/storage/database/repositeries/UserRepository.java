package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

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
