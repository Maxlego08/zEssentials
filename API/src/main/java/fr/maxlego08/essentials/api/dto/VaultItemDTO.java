package fr.maxlego08.essentials.api.dto;

import java.util.UUID;

public record VaultItemDTO(UUID unique_id, int vault_id, int slot, String item, long quantity) {
}
