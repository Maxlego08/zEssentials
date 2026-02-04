package fr.maxlego08.essentials.api;

import fr.maxlego08.essentials.api.chat.ChatCooldown;
import fr.maxlego08.essentials.api.commands.CommandCooldown;
import fr.maxlego08.essentials.api.commands.CommandRestriction;
import fr.maxlego08.essentials.api.config.models.NearDirectionReplacements;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerType;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.utils.MessageColor;
import fr.maxlego08.essentials.api.utils.NearDistance;
import fr.maxlego08.essentials.api.utils.TransformMaterial;
import fr.maxlego08.sarah.DatabaseConfiguration;
import org.bukkit.permissions.Permissible;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the configuration of the plugin.
 * This interface provides methods to access various configuration settings, such as debug mode,
 * command cooldowns, trash size, compact materials, storage type, database configuration,
 * server type, and Redis configuration.
 *
 * @see ConfigurationFile
 */
public interface Configuration extends ConfigurationFile {

    /**
     * Checks if debug mode is enabled in the plugin configuration.
     *
     * @return true if debug mode is enabled, false otherwise.
     */
    boolean isEnableDebug();

    /**
     * Checks if cooldown bypass is enabled in the plugin configuration.
     *
     * @return true if cooldown bypass is enabled, false otherwise.
     */
    boolean isEnableCooldownBypass();

    /**
     * Gets the list of command cooldown configurations from the plugin configuration.
     *
     * @return The list of CommandCooldown objects representing command cooldown configurations.
     */
    List<CommandCooldown> getCommandCooldown();

    /**
     * Gets the list of command restrictions from the plugin configuration.
     *
     * @return The list of {@link CommandRestriction} objects representing command restrictions.
     */
    List<CommandRestriction> getCommandRestrictions();

    /**
     * Gets the cooldown duration for a specific command and permissible entity.
     *
     * @param permissible The permissible entity (e.g., player or command sender).
     * @param command     The name of the command.
     * @return An Optional containing the cooldown duration if found, or empty otherwise.
     */
    Optional<Integer> getCooldown(Permissible permissible, String command);

    /**
     * Gets the size of the trash in the plugin configuration.
     *
     * @return The size of the trash.
     */
    int getTrashSize();

    /**
     * Gets the list of compact material configurations from the plugin configuration.
     *
     * @return The list of CompactMaterial objects representing compact material configurations.
     */
    List<TransformMaterial> getCompactMaterials();

    /**
     * Gets the storage type configured in the plugin.
     *
     * @return The StorageType enum representing the storage type.
     */
    StorageType getStorageType();

    /**
     * Gets the database configuration from the plugin configuration.
     *
     * @return The DatabaseConfiguration representing the database configuration.
     */
    DatabaseConfiguration getDatabaseConfiguration();

    /**
     * Gets the server type configured in the plugin.
     *
     * @return The ServerType enum representing the server type.
     */
    ServerType getServerType();

    /**
     * Gets the Redis configuration from the plugin configuration.
     *
     * @return The RedisConfiguration representing the Redis configuration.
     */
    RedisConfiguration getRedisConfiguration();

    /**
     * Retrieves a list of message colors available.
     *
     * @return a list of {@link MessageColor} objects
     */
    List<MessageColor> getMessageColors();

    /**
     * Retrieves a list of chat cooldown configurations.
     *
     * @return a list of {@link ChatCooldown} objects
     */
    List<ChatCooldown> getCooldowns();

    /**
     * Retrieves an array of command cooldown times.
     *
     * @return an array of long values representing command cooldown times
     */
    long[] getCooldownCommands();

    /**
     * Retrieves a list of smeltable materials.
     *
     * @return a list of {@link TransformMaterial} objects representing smeltable materials
     */
    List<TransformMaterial> getSmeltableMaterials();

    /**
     * Retrieves the default distance used for the "near" command.
     *
     * @return the default near distance as a double
     */
    double getDefaultNearDistance();

    /**
     * Retrieves a list of permissions related to near distance.
     *
     * @return a list of {@link NearDistance} objects representing near permissions
     */
    List<NearDistance> getNearPermissions();

    /**
     * Returns the set of directional replacements (arrows or labels) used to display
     * the relative position of nearby players.
     *
     * @return a {@link NearDirectionReplacements} instance containing direction symbols
     * @since 1.0.3.2
     */
    NearDirectionReplacements getNearDirectionReplacements();

    /**
     * Retrieves the near distance allowed for a specific permissible entity.
     *
     * @param permissible the entity to check permissions for
     * @return the near distance as a double
     */
    double getNearDistance(Permissible permissible);

    /**
     * Returns the world time value used by the /day command.
     *
     * @return the configured day time value
     */
    long getDayTime();

    /**
     * Returns the world time value used by the /night command.
     *
     * @return the configured night time value
     */
    long getNightTime();

    /**
     * Indicates whether world time changes should be animated
     * (e.g. /day, /night commands).
     *
     * @return {@code true} if smooth world time changes are enabled
     */
    boolean isTimeSmoothChangeEnabled();

    /**
     * Checks if command logging is enabled.
     *
     * @return true if command logging is enabled, false otherwise
     */
    boolean isEnableCommandLog();

    /**
     * Retrieves the global date format used across the server.
     *
     * @return the {@link SimpleDateFormat} object representing the global date format
     */
    SimpleDateFormat getGlobalDateFormat();

    /**
     * Retrieves a list of placeholder replacements.
     *
     * @return a list of {@link ReplacePlaceholder} objects
     */
    List<ReplacePlaceholder> getReplacePlaceholders();

    /**
     * Retrieves a specific placeholder replacement by its name.
     *
     * @param placeholder the name of the placeholder to retrieve
     * @return an {@link Optional} containing the found {@link ReplacePlaceholder}, or empty if not found
     */
    Optional<ReplacePlaceholder> getReplacePlaceholder(String placeholder);

    /**
     * Retrieves the default value that should be returned when a placeholder has no data.
     *
     * @return the placeholder empty value configured in {@code config.yml}
     */
    String getPlaceholderEmpty();

    /**
     * Checks if temporary fly tasks are enabled.
     *
     * @return true if temporary fly tasks are enabled, false otherwise
     */
    boolean isTempFlyTask();

    /**
     * Retrieves a list of world names where the fly feature is disabled.
     *
     * @return a list of world names where the fly feature is disabled
     */
    List<String> getDisableFlyWorld();

    /**
     * Checks if fly should be re-enabled when returning to an allowed world.
     *
     * @return true if fly should be re-enabled automatically, false otherwise
     */
    boolean isEnableFlyReturn();

    /**
     * Retrieves a list of random words used by the plugin for various tasks.
     *
     * @return a list of random words
     */
    List<String> getRandomWords();

    /**
     * Checks if the plugin is enabled to display nicknames of online and offline players in the command completion tab.
     *
     * @return true if the plugin is enabled to display nicknames of online and offline players, false otherwise
     */
    boolean isEnableOfflinePlayersName();

    /**
     * Retrieves the batch auto save interval in milliseconds.
     *
     * @return the batch auto save interval in milliseconds
     */
    long getBatchAutoSave();

    /**
     * Retrieves a list of blacklisted UUIDs, which are the UUIDs of the players
     * that are not allowed to connect to the server.
     *
     * @return a list of blacklisted UUIDs
     */
    List<UUID> getBlacklistUuids();

    /**
     * Retrieves a list of intervals in seconds when the plugin should announce the temporary fly task to the player.
     *
     * @return a list of intervals in seconds when the plugin should announce the temporary fly task to the player
     */
    List<Long> getFlyTaskAnnounce();

    /**
     * Retrieves a list of world names where the back feature is disabled.
     *
     * @return a list of world names where the back feature is disabled
     */
    List<String> getDisableBackWorld();

    /**
     * Retrieves the default values of the options, which are the values
     * that are set when a player joins the server for the first time.
     *
     * @return a map of options and their default values
     */
    Map<Option, Boolean> getDefaultOptionValues();

    /**
     * Retrieves the list of commands that should work even if their module is disabled.
     *
     * @return a list of command names that bypass the module disable check
     */
    List<String> getForceCommands();
}
