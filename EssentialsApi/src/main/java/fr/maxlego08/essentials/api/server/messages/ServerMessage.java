package fr.maxlego08.essentials.api.server.messages;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessageType;

import java.util.UUID;

public record ServerMessage(ServerMessageType serverMessageType, UUID uuid, Permission permission, Message message,
                            Object[] arguments) {


}
