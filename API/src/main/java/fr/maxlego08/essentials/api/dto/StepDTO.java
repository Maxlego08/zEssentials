package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

public record StepDTO(UUID unique_id, String step_name, String data, Date created_at, Date updated_at) {
}
