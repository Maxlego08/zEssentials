package fr.maxlego08.essentials.api;

import fr.maxlego08.essentials.api.commands.CommandCooldown;
import fr.maxlego08.essentials.api.configuration.ReplacePlaceholder;
import fr.maxlego08.essentials.api.server.RedisConfiguration;
import fr.maxlego08.essentials.api.server.ServerType;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.utils.ChatCooldown;
import fr.maxlego08.essentials.api.utils.MessageColor;
import fr.maxlego08.essentials.api.utils.NearDistance;
import fr.maxlego08.essentials.api.utils.TransformMaterial;
import fr.maxlego08.sarah.DatabaseConfiguration;
import org.bukkit.permissions.Permissible;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

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

    List<MessageColor> getMessageColors();

    List<ChatCooldown> getCooldowns();

    long[] getCooldownCommands();

    List<TransformMaterial> getSmeltableMaterials();

    double getDefaultNearDistance();

    List<NearDistance> getNearPermissions();

    double getNearDistance(Permissible permissible);

    boolean isEnableCommandLog();

    SimpleDateFormat getGlobalDateFormat();

    List<ReplacePlaceholder> getReplacePlaceholders();

    Optional<ReplacePlaceholder> getReplacePlaceholder(String placeholder);
}
