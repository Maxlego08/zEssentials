package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.UserDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.List;
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

    public List<UserDTO> selectOptions(String userName) {
        return select(UserDTO.class, table -> table.where("name", userName));
    }

    public long totalUsers() {
        return select(schema -> {
        });
    }

    public boolean userAlreadyExist(UUID uniqueId) {
        return select(schema -> {
            schema.where("unique_id", uniqueId);
        }) != 0;
    }
}
