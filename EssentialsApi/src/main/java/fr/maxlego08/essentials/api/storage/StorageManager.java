package fr.maxlego08.essentials.api.storage;

import org.bukkit.event.Listener;

public interface StorageManager extends Listener {

    void onEnable();

    void onDisable();

    IStorage getStorage();

    StorageType getType();

}
