package fr.maxlego08.essentials.api.modules;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * Represents a manager for handling modules in the plugin system.
 */
public interface ModuleManager {

    /**
     * Loads all modules.
     */
    void loadModules();

    /**
     * Loads configurations for all modules.
     */
    void loadConfigurations();

    /**
     * Gets the module of the specified type.
     *
     * @param module The class representing the type of module to retrieve.
     * @param <T>    The type of module.
     * @return The module of the specified type, or null if not found.
     */
    <T extends Module> T getModule(Class<T> module);

    YamlConfiguration getModuleConfiguration(String module);

    /**
     * Gets the folder associated with the module manager.
     *
     * @return The folder associated with the module manager.
     */
    File getFolder();

    /**
     * Gets all modules.
     *
     * @return A list of all modules.
     */
    List<Module> getModules();
}

