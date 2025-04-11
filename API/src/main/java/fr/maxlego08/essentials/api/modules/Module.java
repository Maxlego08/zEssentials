package fr.maxlego08.essentials.api.modules;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

/**
 * Represents a module in the plugin system.
 */
public interface Module extends Listener {

    /**
     * Gets the name of the module.
     *
     * @return The name of the module.
     */
    String getName();

    /**
     * Loads the configuration for the module.
     */
    void loadConfiguration();

    /**
     * Retrieves the configuration file associated with the module.
     *
     * @return The configuration file.
     */
    File getConfigurationFile();

    /**
     * Gets the folder associated with the module.
     *
     * @return The folder associated with the module.
     */
    File getFolder();

    /**
     * Gets the configuration for the module.
     *
     * @return The configuration for the module.
     */
    YamlConfiguration getConfiguration();

    /**
     * Checks if the module is enabled.
     *
     * @return true if the module is enabled, false otherwise.
     */
    boolean isEnable();

    /**
     * Checks if the module registers events.
     *
     * @return true if the module registers events, false otherwise.
     */
    boolean isRegisterEvent();

}

