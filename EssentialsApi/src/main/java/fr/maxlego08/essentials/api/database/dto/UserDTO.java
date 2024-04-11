package fr.maxlego08.essentials.api.database.dto;

import java.util.UUID;

public record UserDTO(UUID unique_id, String name) {
}
