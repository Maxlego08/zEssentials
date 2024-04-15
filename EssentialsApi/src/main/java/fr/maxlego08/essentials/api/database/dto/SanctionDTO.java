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
    Date finish_at,
    SanctionType sanction_type
) {}
