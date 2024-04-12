package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.database.dto.ServerStorageDTO;
import fr.maxlego08.essentials.api.storage.ServerStorage;
import org.bukkit.Location;

import java.util.List;

public class ZServerStorage extends ZUtils implements ServerStorage {

    private Location spawnLocation;

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    @Override
    public void setContents(List<ServerStorageDTO> serverStorageDTOS) {
        serverStorageDTOS.forEach(serverStorageDTO -> {
            switch (serverStorageDTO.name()) {
                case "spawn_location" -> this.spawnLocation = stringAsLocation(serverStorageDTO.content());
                default -> throw new Error("Name " + serverStorageDTO.name() + " was not found !");
            }
        });
    }
}
