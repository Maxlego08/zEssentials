package fr.maxlego08.essentials.api.server.messages;

import java.util.UUID;

public record ServerPrivateMessage(UUID senderUniqueId, String senderName, UUID targetUniqueId, String message) {
}
