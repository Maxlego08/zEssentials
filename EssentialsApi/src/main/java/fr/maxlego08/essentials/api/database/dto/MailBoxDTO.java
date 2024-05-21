package fr.maxlego08.essentials.api.database.dto;

import java.util.Date;
import java.util.UUID;

public record MailBoxDTO(int id, UUID unique_id, String itemstack, Date expired_at, Date created_at) {
}
