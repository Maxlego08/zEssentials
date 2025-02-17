package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

public record PrivateMessageDTO(UUID sender_unique_id, UUID receiver_unique_id, String content, Date created_at) {
}
