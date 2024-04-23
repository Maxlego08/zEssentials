package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.UUID;

public class UserPlayTimeRepository extends Repository {
    public UserPlayTimeRepository(SqlConnection connection) {
        super(connection, "user_play_times");
    }

    public void insert(UUID uuid, long playtime, String address) {
        insert(table -> {
            table.uuid("unique_id", uuid);
            table.decimal("play_time", playtime);
            table.string("address", address);
        });
    }
}
