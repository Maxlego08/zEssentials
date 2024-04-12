package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.database.dto.ServerStorageDTO;
import org.bukkit.Location;

import java.util.List;

public interface ServerStorage {

    Location getSpawnLocation();

    void setSpawnLocation(Location spawnLocation);

    void setContents(List<ServerStorageDTO> serverStorageDTOS);
}
