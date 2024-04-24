package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.PlayTimeDTO;
import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.List;
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

    public List<PlayTimeDTO> select(UUID uuid) {
        return this.select(PlayTimeDTO.class, table -> table.where("unique_id", uuid));
    }
}
