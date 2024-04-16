package fr.maxlego08.essentials.api.server;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;

import java.util.UUID;

public class ServerMessage extends IdentifiedInstance {
    private ServerMessageType messageType;
    private UUID uniqueId;
    private Permission permission;
    private Message message;
    private Object[] attachments;

    public ServerMessage(UUID instanceId, ServerMessageType messageType, UUID uniqueId, Permission permission, Message message, Object[] attachments) {
        super(instanceId);
        this.messageType = messageType;
        this.uniqueId = uniqueId;
        this.permission = permission;
        this.message = message;
        this.attachments = attachments;
    }

    public ServerMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ServerMessageType messageType) {
        this.messageType = messageType;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Object[] attachments) {
        this.attachments = attachments;
    }
}
