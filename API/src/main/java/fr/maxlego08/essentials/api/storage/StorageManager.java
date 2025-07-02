package fr.maxlego08.essentials.api.storage;

import org.bukkit.event.Listener;

public interface StorageManager extends Listener {

    /**
     * Enables the storage manager.
     */
    void onEnable();

    /**
     * Disables the storage manager.
     */
    void onDisable();

    /**
     * Retrieves the storage instance.
     *
     * @return the storage instance
     */
    IStorage getStorage();

    /**
     * Retrieves the type of the storage.
     *
     * @return the type of the storage
     */
    StorageType getType();
}
