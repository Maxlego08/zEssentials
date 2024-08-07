package fr.maxlego08.essentials.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserEconomyRankingDTO(UUID unique_id, String name, BigDecimal amount) {
}
