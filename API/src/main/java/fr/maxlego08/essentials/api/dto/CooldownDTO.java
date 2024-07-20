package fr.maxlego08.essentials.api.dto;

import java.util.Date;

public record CooldownDTO(String cooldown_name, long cooldown_value, Date created_at) {
}
