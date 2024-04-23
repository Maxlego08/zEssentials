package fr.maxlego08.essentials.api.database.dto;

import java.util.Date;
import java.util.UUID;

public record PlayTimeDTO(UUID unique_id, long play_time, String address, Date created_at) {
}
