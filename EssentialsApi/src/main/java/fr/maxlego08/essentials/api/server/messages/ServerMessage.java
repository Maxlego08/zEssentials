package fr.maxlego08.essentials.api.server.messages;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.user.Option;

import java.util.UUID;

public record ServerMessage(ServerMessageType serverMessageType, UUID uuid, Permission permission, Option option,
                            Message message,
                            Object[] arguments) {

    public static ServerMessage single(UUID uuid, Message message, Object... objects) {
        return new ServerMessage(ServerMessageType.SINGLE, uuid, null, null, message, objects);
    }

}
