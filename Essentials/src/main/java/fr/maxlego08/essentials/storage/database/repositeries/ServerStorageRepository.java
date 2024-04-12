package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.database.dto.ServerStorageDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.essentials.storage.database.SqlConnection;

import java.util.List;

public class ServerStorageRepository extends Repository {

    public ServerStorageRepository(SqlConnection connection) {
        super(connection, "storages");
    }

    public void upsert(String key, Object value) {
        upsert(table -> {
            table.string("name", key);
            table.string("content", value.toString());
        });
    }

    public List<ServerStorageDTO> select() {
        return select(ServerStorageDTO.class, table -> {
        });
    }
}
