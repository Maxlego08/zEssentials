package fr.maxlego08.essentials.api.database.dto;

import fr.maxlego08.essentials.api.sanction.SanctionType;

import java.util.UUID;
import java.util.Date;

public record SanctionDTO(
    int id,
    UUID player_unique_id,
    UUID sender_unique_id,
    String reason,
    Date created_at,
    Date expired_at,
    SanctionType sanction_type,
    long duration
) {
    public boolean isActive() {
        return this.expired_at.getTime() > System.currentTimeMillis();
    }
}
