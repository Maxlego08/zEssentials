package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.UUID;

public class ZEssentialsUtils extends ZUtils implements EssentialsUtils {
    @Override
    public void message(UUID uniqueId, Message message, Object... args) {
        super.message(uniqueId, message, args);
    }

    @Override
    public void message(User sender, Message message, Object... args) {
        super.message(sender, message, args);
    }

    @Override
    public void message(CommandSender sender, Message message, Object... args) {
        super.message(sender, message, args);
    }

    @Override
    public void broadcast(Permission permission, Message message, Object... args) {
        super.broadcast(permission, message, args);
    }

    @Override
    public void process(ServerMessage receivedMessage) {
        if (receivedMessage.serverMessageType() == ServerMessageType.BROADCAST) {
            this.broadcast(receivedMessage.permission(), receivedMessage.message(), receivedMessage.arguments());
        } else {
            this.message(receivedMessage.uuid(), receivedMessage.message(), receivedMessage.arguments());
        }
    }

    @Override
    public Component getComponentMessage(Message message, Object... objects) {
        return super.getComponentMessage(message, objects);
    }

    @Override
    public Component getComponentMessage(String message) {
        return this.componentMessage.getComponent(message);
    }

    @Override
    public String getMessage(Message message, Object... objects) {
        return super.getMessage(message, objects);
    }

    @Override
    public Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map) {
        return super.createInstanceFromMap(constructor, map);
    }
}
