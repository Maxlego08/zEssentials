package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.ServerStorageDTO;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.List;

public class ServerStorageRepository extends Repository {

    public ServerStorageRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "storages");
    }

    public void upsert(String key, Object value) {
        upsert(table -> {
            table.string("name", key).primary();
            table.string("content", value.toString());
        });
    }

    public List<ServerStorageDTO> select() {
        return select(ServerStorageDTO.class, table -> {
        });
    }
}
