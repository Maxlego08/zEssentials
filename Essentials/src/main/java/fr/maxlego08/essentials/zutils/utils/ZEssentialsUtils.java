package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessage;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import org.bukkit.command.CommandSender;

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
        if (receivedMessage.getMessageType() == ServerMessageType.BROADCAST){
            this.broadcast(receivedMessage.getPermission(), receivedMessage.getMessage(), receivedMessage.getAttachments());
        } else {
            this.message(receivedMessage.getUniqueId(), receivedMessage.getMessage(), receivedMessage.getAttachments());
        }
    }


}
