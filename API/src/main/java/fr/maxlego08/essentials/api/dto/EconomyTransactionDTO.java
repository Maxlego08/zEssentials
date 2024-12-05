package fr.maxlego08.essentials.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record EconomyTransactionDTO(
        UUID from_unique_id,
        UUID to_unique_id,
        String economy_name,
        String reason,
        BigDecimal amount,
        BigDecimal from_amount,
        BigDecimal to_amount,
        Date created_at,
        Date updated_at
) {
}
