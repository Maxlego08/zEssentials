package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.UUID;

/**
 * Utility methods provided by Essentials.
 * This interface defines various utility methods for handling messages, commands, and instances.
 */
public interface EssentialsUtils {

    /**
     * Sends a message to the player with the specified UUID.
     *
     * @param uniqueId The UUID of the player.
     * @param message  The message to send.
     * @param args     Arguments to format into the message.
     */
    void message(UUID uniqueId, Message message, Object... args);

    /**
     * Sends a message to the specified user.
     *
     * @param sender  The user to send the message to.
     * @param message The message to send.
     * @param args    Arguments to format into the message.
     */
    void message(User sender, Message message, Object... args);

    /**
     * Sends a message to the specified command sender.
     *
     * @param sender  The command sender to send the message to.
     * @param message The message to send.
     * @param args    Arguments to format into the message.
     */
    void message(CommandSender sender, Message message, Object... args);

    /**
     * Broadcasts a message to all players with the specified permission.
     *
     * @param permission The permission required to receive the message.
     * @param message    The message to broadcast.
     * @param args       Arguments to format into the message.
     */
    void broadcast(Permission permission, Message message, Object... args);

    /**
     * Processes a server message received from the server.
     *
     * @param receivedMessage The server message to process.
     */
    void process(ServerMessage receivedMessage);

    /**
     * Formats a message into a plain text string.
     *
     * @param message The message to format.
     * @param objects Objects to format into the message.
     * @return The formatted plain text message.
     */
    String getMessage(Message message, Object... objects);

    /**
     * Creates an instance of a class using the specified constructor and map of parameters.
     *
     * @param constructor The constructor to use for creating the instance.
     * @param map         The map of parameters to pass to the constructor.
     * @return The created instance.
     */
    Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map);

    /**
     * Enables or disables the global chat.
     *
     * @param value true to enable the chat, false to disable it.
     */
    void toggleChat(boolean value);

    /**
     * Broadcasts a message to all players in the server.
     *
     * @param message   The message to broadcast.
     * @param arguments Arguments to format into the message.
     */
    void broadcast(Message message, Object... arguments);

    /**
     * Kicks a player from the server with a specified message.
     *
     * @param player  The player to be kicked from the server.
     * @param message The message to send to the player upon being kicked.
     * @param objects Additional arguments to format into the message.
     */
    void kick(Player player, Message message, Object... objects);

    /**
     * Prevents a player from logging in by setting the result of the specified {@link PlayerLoginEvent} to the specified {@link PlayerLoginEvent.Result}.
     * A message is sent to the player with the specified {@link Message} and format arguments.
     *
     * @param event   The {@link PlayerLoginEvent} to modify.
     * @param result  The {@link PlayerLoginEvent.Result} to set on the event.
     * @param message The message to send to the player.
     * @param objects The format arguments to pass to the message.
     */
    void disallow(PlayerLoginEvent event, PlayerLoginEvent.Result result, Message message, Object... objects);

    /**
     * Sends a private message from the specified user to the recipient.
     *
     * @param user           The user sending the private message.
     * @param privateMessage The type of private message to send.
     * @param message        The message used for the private message.
     * @param content        The content of the private message.
     */
    void sendPrivateMessage(User user, PrivateMessage privateMessage, Message message, String content);

    /**
     * Broadcasts a message to all players based on the specified option.
     *
     * @param option  The option that determines the criteria for broadcasting the message.
     * @param message The message to broadcast.
     * @param objects Additional arguments to format into the message.
     */
    void broadcast(Option option, Message message, Object... objects);

    /**
     * Deletes a specified cooldown for a player.
     *
     * @param uniqueId     The UUID of the player whose cooldown is to be deleted.
     * @param cooldownName The name of the cooldown to delete.
     */
    void deleteCooldown(UUID uniqueId, String cooldownName);

    /**
     * Updates the expiration time of a specified cooldown for a player.
     *
     * @param uniqueId     The UUID of the player whose cooldown is to be updated.
     * @param cooldownName The name of the cooldown to update.
     * @param expiredAt    The new expiration time for the cooldown, in milliseconds.
     */
    void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt);
}
