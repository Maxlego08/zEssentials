package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

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
     * Converts a message into a Adventure Component.
     *
     * @param message The message to convert.
     * @param objects Objects to format into the message.
     * @return The Adventure Component representing the message.
     */
    Component getComponentMessage(Message message, Object... objects);

    /**
     * Converts a plain text message into a Adventure Component.
     *
     * @param message The plain text message to convert.
     * @return The Adventure Component representing the message.
     */
    Component getComponentMessage(String message);

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
}
