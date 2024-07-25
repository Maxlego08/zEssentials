package fr.maxlego08.essentials.api.dto;

import java.util.UUID;

public record VaultDTO(UUID unique_id, int vault_id, String name, String icon) {
}
