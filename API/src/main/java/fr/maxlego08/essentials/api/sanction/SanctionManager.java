package fr.maxlego08.essentials.api.sanction;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.time.Duration;
import java.util.UUID;

public interface SanctionManager {

    /**
     * Returns the UUID of the sender, or null if the sender is not a player.
     *
     * @param sender The sender to get the UUID for.
     * @return The UUID of the sender, or null if the sender is not a player.
     */
    UUID getSenderUniqueId(CommandSender sender);

    /**
     * Kick a player with a specified reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to kick.
     * @param playerName The name of the player to kick.
     * @param reason     The reason for the kick.
     */
    void kick(CommandSender sender, UUID uuid, String playerName, String reason);

    /**
     * Ban a player for a specified duration with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to ban.
     * @param playerName The name of the player to ban.
     * @param duration   The duration of the ban.
     * @param reason     The reason for the ban.
     */
    void ban(CommandSender sender, UUID uuid, String playerName, Duration duration, String reason);

    /**
     * Mute a player for a specified duration with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to mute.
     * @param playerName The name of the player to mute.
     * @param duration   The duration of the mute.
     * @param reason     The reason for the mute.
     */
    void mute(CommandSender sender, UUID uuid, String playerName, Duration duration, String reason);

    /**
     * UnMute a player with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to unmute.
     * @param playerName The name of the player to unmute.
     * @param reason     The reason for to unmute.
     */
    void unmute(CommandSender sender, UUID uuid, String playerName, String reason);

    /**
     * Process the unmute command.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to unmute.
     * @param playerName The name of the player to unmute.
     * @param reason     The reason for the unmute.
     */
    void processUnmute(CommandSender sender, UUID uuid, String playerName, String reason);

    /**
     * UnBan a player with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to unban.
     * @param playerName The name of the player to unban.
     * @param reason     The reason for the unban.
     */
    void unban(CommandSender sender, UUID uuid, String playerName, String reason);

    /**
     * Open the sanction interface for a specified user.
     *
     * @param user     The user to open the sanction interface for.
     * @param uuid     The UUID of the player to sanction.
     * @param userName The name of the player to sanction.
     */
    void openSanction(User user, UUID uuid, String userName);

    /**
     * Retrieves the name of the player who issued the last sanction, given the UUID of the sender.
     *
     * @param senderUniqueId The UUID of the sender who issued the last sanction.
     * @return The name of the player who issued the last sanction, or null if no player issued the last sanction.
     */
    String getSanctionBy(UUID senderUniqueId);

    /**
     * Retrieves the name of the player who issued the last sanction.
     *
     * @param sender The command sender.
     * @return The name of the player who issued the last sanction, or null if no player issued the last sanction.
     */
    String getSanctionBy(CommandSender sender);

    /**
     * Checks if a user is protected from certain actions or sanctions.
     *
     * @param username The username of the player to check.
     * @return true if the user is protected, false otherwise.
     */
    boolean isProtected(String username);

    /**
     * Retrieves and displays the last seen information of a player.
     *
     * @param sender The command sender requesting the information.
     * @param uuid   The UUID of the player whose last seen information is to be retrieved.
     */
    void seen(CommandSender sender, UUID uuid);

    /**
     * Lists all players who have been seen using the specified IP address.
     *
     * @param sender The command sender.
     * @param ip     The IP address to search for.
     */
    void seen(CommandSender sender, String ip);

    /**
     * Freezes a player, preventing them from moving or performing actions.
     *
     * @param sender   The command sender issuing the freeze.
     * @param uuid     The UUID of the player to freeze.
     * @param userName The name of the player to freeze.
     */
    void freeze(CommandSender sender, UUID uuid, String userName);

    /**
     * Cancels a chat event for a specific player, if necessary.
     *
     * @param event  The cancellable chat event to be potentially cancelled.
     * @param player The player involved in the chat event.
     */
    void cancelChatEvent(Cancellable event, Player player);
}
