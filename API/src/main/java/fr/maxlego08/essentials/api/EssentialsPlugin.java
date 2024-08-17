package fr.maxlego08.essentials.api;

import com.google.gson.Gson;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.chat.InteractiveChat;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.enchantment.Enchantments;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.storage.ServerStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.essentials.api.vault.VaultManager;
import fr.maxlego08.essentials.api.vote.VoteManager;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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
     * Gets the economy manager for handling economy-related operations.
     *
     * @return The economy provider.
     */
    EconomyManager getEconomyManager();

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

    /**
     * Opens a specified inventory for a player.
     *
     * @param player        the player to open the inventory for
     * @param inventoryName the name of the inventory to open
     */
    void openInventory(Player player, String inventoryName);

    /**
     * Saves or updates the server configuration.
     *
     * @param toPath the path where the configuration should be saved
     * @param deep   whether to perform a deep save
     */
    void saveOrUpdateConfiguration(String toPath, boolean deep);

    /**
     * Saves or updates the server configuration from a specified resource path.
     *
     * @param resourcePath the resource path to save or update from
     * @param toPath       the path where the configuration should be saved
     * @param deep         whether to perform a deep save
     */
    void saveOrUpdateConfiguration(String resourcePath, String toPath, boolean deep);

    /**
     * Retrieves a kit by its name.
     *
     * @param kitName the name of the kit to retrieve
     * @return an {@link Optional} containing the kit if found, or empty if not found
     */
    Optional<Kit> getKit(String kitName);

    /**
     * Gives a kit to a user, optionally bypassing the cooldown.
     *
     * @param user            the user to give the kit to
     * @param kit             the kit to give
     * @param bypassCooldown  whether to bypass the kit cooldown
     */
    void giveKit(User user, Kit kit, boolean bypassCooldown);

    /**
     * Retrieves a list of materials.
     *
     * @return a list of {@link Material} objects
     */
    List<Material> getMaterials();

    /**
     * Retrieves the scoreboard manager.
     *
     * @return the {@link ScoreboardManager} instance
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Gives an item to a player.
     *
     * @param player    the player to give the item to
     * @param itemStack the item to give
     */
    void give(Player player, ItemStack itemStack);

    /**
     * Retrieves the hologram manager.
     *
     * @return the {@link HologramManager} instance
     */
    HologramManager getHologramManager();

    /**
     * Retrieves the component message manager.
     *
     * @return the {@link ComponentMessage} instance
     */
    ComponentMessage getComponentMessage();

    /**
     * Processes a string using PlaceholderAPI for a specific player.
     *
     * @param player the player to process the string for
     * @param string the string to process
     * @return the processed string
     */
    String papi(Player player, String string);

    /**
     * Retrieves the vote manager.
     *
     * @return the {@link VoteManager} instance
     */
    VoteManager getVoteManager();

    /**
     * Retrieves the vault manager.
     *
     * @return the {@link VaultManager} instance
     */
    VaultManager getVaultManager();

    /**
     * Retrieves the world edit manager.
     *
     * @return the {@link WorldeditManager} instance
     */
    WorldeditManager getWorldeditManager();

    /**
     * Starts an interactive chat session with a player.
     *
     * @param player   the player to start the chat session with
     * @param consumer the consumer to handle the chat input
     * @param expiredAt the time when the chat session expires
     * @return the {@link InteractiveChat} instance
     */
    InteractiveChat startInteractiveChat(Player player, Consumer<String> consumer, long expiredAt);

    /**
     * Retrieves the enchantments manager.
     *
     * @return the {@link Enchantments} instance
     */
    Enchantments getEnchantments();

    /**
     * Creates an instance of a specified class.
     *
     * @param className the name of the class to create an instance of
     * @param <T>       the type of the instance
     * @return an {@link Optional} containing the instance if successful, or empty if not
     */
    <T> Optional<T> createInstance(String className);

    /**
     * Creates an instance of a specified class with an option to display a log.
     *
     * @param className  the name of the class to create an instance of
     * @param displayLog whether to display a log message
     * @param <T>        the type of the instance
     * @return an {@link Optional} containing the instance if successful, or empty if not
     */
    <T> Optional<T> createInstance(String className, boolean displayLog);
}
