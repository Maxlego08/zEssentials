package fr.maxlego08.essentials.api.database.dto;

import java.util.Date;
import java.util.UUID;

public record CommandDTO(UUID unique_id, String command, Date created_at) {
}
