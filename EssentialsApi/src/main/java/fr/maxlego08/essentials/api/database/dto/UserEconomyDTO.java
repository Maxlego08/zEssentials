package fr.maxlego08.essentials.api.database.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserEconomyDTO(UUID unique_id, String economy_name, BigDecimal amount) {
}
