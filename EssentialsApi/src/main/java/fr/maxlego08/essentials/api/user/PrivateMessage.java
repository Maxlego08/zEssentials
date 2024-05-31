package fr.maxlego08.essentials.api.user;

import java.util.UUID;

public record PrivateMessage(UUID uuid, String username) {
}
