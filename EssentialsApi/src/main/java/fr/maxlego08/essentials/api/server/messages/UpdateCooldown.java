package fr.maxlego08.essentials.api.server.messages;

import java.util.UUID;

public record UpdateCooldown(UUID uniqueId, String cooldownName, long expiredAt) {
}
