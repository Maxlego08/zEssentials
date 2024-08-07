package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

public record ChatMessageDTO(UUID unique_id, String content, Date created_at) {
}
