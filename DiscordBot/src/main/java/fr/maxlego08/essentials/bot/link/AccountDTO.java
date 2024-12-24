package fr.maxlego08.essentials.bot.link;

import java.util.UUID;

public record AccountDTO(long user_id, UUID unique_id, String username) {
}
