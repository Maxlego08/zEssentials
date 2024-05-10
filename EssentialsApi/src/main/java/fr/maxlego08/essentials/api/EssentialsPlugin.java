package fr.maxlego08.essentials.api;

import com.google.gson.Gson;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.storage.ServerStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the essentials plugin.
 */
public interface EssentialsPlugin extends Plugin {

    /**
     * Gets the command manager for handling plugin commands.
     *
     * @return The command manager.
     */
    CommandManager getCommandManager();

    /**
     * Gets the storage manager for handling data storage.
     *
     * @return The storage manager.
     */
    StorageManager getStorageManager();

    /**
     * Gets a list of configuration files used by the plugin.
     *
     * @return A list of configuration files.
     */
    List<ConfigurationFile> getConfigurationFiles();

    /**
     * Gets the Gson instance used for JSON serialization and deserialization.
     *
     * @return The Gson instance.
     */
    Gson getGson();

    /**
     * Gets the Persist instance for handling persistent storage.
     *
     * @return The Persist instance.
     */
    Persist getPersist();

    /**
     * Gets the scheduler implementation for scheduling tasks.
     *
     * @return The scheduler implementation.
     */
    ServerImplementation getScheduler();

    /**
     * Gets the module manager for managing plugin modules.
     *
     * @return The module manager.
     */
    ModuleManager getModuleManager();

    /**
     * Gets the inventory manager for managing inventories.
     *
     * @return The inventory manager.
     */
    InventoryManager getInventoryManager();

    /**
     * Gets the button manager for managing buttons in GUIs.
     *
     * @return The button manager.
     */
    ButtonManager getButtonManager();

    /**
     * Gets the pattern manager for managing patterns in GUIs.
     *
     * @return The pattern manager.
     */
    PatternManager getPatternManager();

    /**
     * Gets the placeholder manager for handling placeholders.
     *
     * @return The placeholder manager.
     */
    Placeholder getPlaceholder();

    /**
     * Gets the configuration for the plugin.
     *
     * @return The configuration.
     */
    Configuration getConfiguration();

    /**
     * Checks if the economy feature is enabled.
     *
     * @return true if the economy feature is enabled, false otherwise.
     */
    boolean isEconomyEnable();

    /**
     * Gets the economy provider for handling economy-related operations.
     *
     * @return The economy provider.
     */
    EconomyProvider getEconomyProvider();

    /**
     * Gets the unique ID of the console.
     *
     * @return The unique ID of the console.
     */
    UUID getConsoleUniqueId();

    /**
     * Gets the server storage for handling server-related data.
     *
     * @return The server storage.
     */
    ServerStorage getServerStorage();

    /**
     * Sets the server storage for handling server-related data.
     *
     * @param serverStorage The server storage to set.
     */
    void setServerStorage(ServerStorage serverStorage);

    /**
     * Checks if Folia integration is enabled.
     *
     * @return true if Folia integration is enabled, false otherwise.
     */
    boolean isFolia();

    /**
     * Gets a list of all warps available.
     *
     * @return A list of all warps.
     */
    List<Warp> getWarps();

    /**
     * Gets a warp by its name.
     *
     * @param name The name of the warp.
     * @return An optional containing the warp if found, otherwise empty.
     */
    Optional<Warp> getWarp(String name);

    /**
     * Gets the maximum number of homes allowed for a permissible entity.
     *
     * @param permissible The permissible entity.
     * @return The maximum number of homes allowed.
     */
    int getMaxHome(Permissible permissible);

    /**
     * Gets a user by their unique ID.
     *
     * @param uniqueId The unique ID of the user.
     * @return The user with the specified unique ID.
     */
    User getUser(UUID uniqueId);

    /**
     * Gets the essentials server instance.
     *
     * @return The essentials server instance.
     */
    EssentialsServer getEssentialsServer();

    /**
     * Gets the utility class for essential operations.
     *
     * @return The utility class.
     */
    EssentialsUtils getUtils();

    /**
     * Logs a debug message.
     *
     * @param message The debug message to log.
     */
    void debug(String message);

    void openInventory(Player player, String inventoryName);

    void saveOrUpdateConfiguration(String toPath);

    void saveOrUpdateConfiguration(String resourcePath, String toPath);

    Optional<Kit> getKit(String kitName);

    void giveKit(User user, Kit kit, boolean bypassCooldown);

    List<Material> getMaterials();
}
