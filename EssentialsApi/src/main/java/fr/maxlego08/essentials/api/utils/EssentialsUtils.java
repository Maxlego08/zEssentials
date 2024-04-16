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

public interface EssentialsUtils {

    void message(UUID uniqueId, Message message, Object... args);

    void message(User sender, Message message, Object... args);

    void message(CommandSender sender, Message message, Object... args);

    void broadcast(Permission permission, Message message, Object... args);

    void process(ServerMessage receivedMessage);

    Component getComponentMessage(Message message, Object... objects);

    Component getComponentMessage(String message);

    String getMessage(Message message, Object... objects);

    Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map);
}
