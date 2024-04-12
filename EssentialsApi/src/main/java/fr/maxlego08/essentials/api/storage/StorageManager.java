package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.database.dto.ServerStorageDTO;
import org.bukkit.event.Listener;

import java.util.List;

public interface StorageManager extends Listener {

    void onEnable();

    void onDisable();

    IStorage getStorage();

    StorageType getType();
}
