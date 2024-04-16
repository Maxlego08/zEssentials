package fr.maxlego08.essentials.api.database.dto;

import java.util.UUID;

public record UserDTO(UUID unique_id, String name, String last_location, Integer ban_sanction_id, Integer mute_sanction_id) {
}
