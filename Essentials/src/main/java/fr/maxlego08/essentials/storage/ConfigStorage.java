package fr.maxlego08.essentials.storage;


import fr.maxlego08.essentials.api.storage.Persist;
import org.bukkit.Location;

public class ConfigStorage {

    public static Location spawnLocation;

    /**
     * static Singleton instance.
     */
    private static volatile ConfigStorage instance;


    /**
     * Private constructor for singleton.
     */
    private ConfigStorage() {
    }

    /**
     * Return a singleton instance of Config.
     */
    public static ConfigStorage getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (ConfigStorage.class) {
                if (instance == null) {
                    instance = new ConfigStorage();
                }
            }
        }
        return instance;
    }

    public void save(Persist persist) {
        persist.save(getInstance());
    }

    public void load(Persist persist) {
        persist.loadOrSaveDefault(getInstance(), ConfigStorage.class);
    }

}
