package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

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

    void toggleChat(boolean value);

    void broadcast(Message message, Object... arguments);

    void kick(Player player, Message message, Object... objects);

    void disallow(PlayerLoginEvent event, PlayerLoginEvent.Result result, Message message, Object... objects);

    void sendPrivateMessage(User user, PrivateMessage privateMessage, Message message, String content);

    void broadcast(Option option, Message message, Object... objects);

    void deleteCooldown(UUID uniqueId, String cooldownName);

    void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt);
}
