package fr.maxlego08.essentials.api.server.messages;

import fr.maxlego08.essentials.api.messages.Message;

import java.util.UUID;

public record KickMessage(UUID playerUniqueId, Message message, Object[] arguments) {
}
