package fr.maxlego08.essentials.api.server.messages;

import java.util.UUID;

public record ClearCooldown(UUID uniqueId, String cooldownName) {
}
