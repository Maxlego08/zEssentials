package fr.maxlego08.essentials.api.server;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.teleport.CrossServerLocation;
import fr.maxlego08.essentials.api.teleport.TeleportType;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Represents the server of the plugin. This interface provides some methods to
 * interact with the server such as sending messages to players, kicking players,
 * clearing the chat, toggling the chat and broadcasting messages.
 *
 * @author MaxLeg08
 */
public interface EssentialsServer {

    /**
     * Called when the plugin is enabled.
     */
    void onEnable();

    /**
     * Called when the plugin is disabled.
     */
    void onDisable();

    /**
     * Gets the list of all online players names.
     *
     * @return The list of all online players names.
     */
    List<String> getPlayersNames();

    /**
     * Sends a message to a player with the given UUID.
     *
     * @param uuid     The UUID of the player.
     * @param message  The message to send.
     * @param objects  The objects to replace in the message.
     */
    void sendMessage(UUID uuid, Message message, Object... objects);

    /**
     * Broadcasts a message to all players with the given permission.
     *
     * @param permission The permission required to receive the message.
     * @param message     The message to broadcast.
     * @param objects     The objects to replace in the message.
     */
    void broadcastMessage(Permission permission, Message message, Object... objects);

    /**
     * Broadcasts a message to all players with the given option.
     *
     * @param option  The option required to receive the message.
     * @param message The message to broadcast.
     * @param objects The objects to replace in the message.
     */
    void broadcastMessage(Option option, Message message, Object... objects);

    /**
     * Kicks a player with the given UUID.
     *
     * @param uuid     The UUID of the player to kick.
     * @param message  The message to send to the player.
     * @param objects  The objects to replace in the message.
     */
    void kickPlayer(UUID uuid, Message message, Object... objects);

    /**
     * Checks if a player is online.
     *
     * @param userName The name of the player to check.
     * @return true if the player is online, false otherwise.
     */
    boolean isOnline(String userName);

    /**
     * Clears the chat of the given sender.
     *
     * @param sender The sender of the command.
     */
    void clearChat(CommandSender sender);

    /**
     * Toggles the chat of the server.
     *
     * @param value The value of the chat (true = enabled, false = disabled).
     */
    void toggleChat(boolean value);

    /**
     * Broadcasts a message to all players.
     *
     * @param message The message to broadcast.
     */
    void broadcast(String message);

    /**
     * Sends a private message to a player.
     *
     * @param user       The user to send the message to.
     * @param privateMessage The private message to send.
     * @param message     The message to send.
     */
    void sendPrivateMessage(User user, PrivateMessage privateMessage, String message);

    /**
     * Deletes a cooldown for a player.
     *
     * @param uniqueId  The UUID of the player.
     * @param cooldownName The name of the cooldown to delete.
     */
    void deleteCooldown(UUID uniqueId, String cooldownName);

    /**
     * Updates a cooldown for a player.
     *
     * @param uniqueId  The UUID of the player.
     * @param cooldownName The name of the cooldown to update.
     * @param expiredAt The time when the cooldown will expire.
     */
    void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt);

    /**
     * Gets the list of all offline players names.
     *
     * @return The list of all offline players names.
     */
    List<String> getOfflinePlayersNames();

    /**
     * Sends a public message to all players in the same world as the given player.
     *
     * @param player The player to send the message from.
     * @param message The message to send.
     */
    void pub(Player player, String message);

    /**
     * Sends a player to another server via BungeeCord/Velocity.
     *
     * @param player The player to send.
     * @param serverName The target server name.
     */
    void sendToServer(Player player, String serverName);

    /**
     * Requests a cross-server teleport. The player will be sent to the target server
     * and then teleported to the destination location.
     *
     * @param player The player to teleport.
     * @param teleportType The type of teleportation.
     * @param destination The destination location including server name.
     */
    void crossServerTeleport(Player player, TeleportType teleportType, CrossServerLocation destination);

    /**
     * Requests a cross-server teleport to another player.
     *
     * @param player The player requesting the teleport.
     * @param teleportType The type of teleportation (TPA or TPA_HERE).
     * @param targetPlayerName The name of the target player.
     * @param targetServer The server where the target player is.
     */
    void crossServerTeleportToPlayer(Player player, TeleportType teleportType, String targetPlayerName, String targetServer);

    /**
     * Gets the current server name from BungeeCord configuration.
     *
     * @return The server name or null if not configured.
     */
    String getServerName();

    /**
     * Finds which server a player is on.
     *
     * @param playerName The player name to find.
     * @return The server name or null if not found.
     */
    String findPlayerServer(String playerName);
}