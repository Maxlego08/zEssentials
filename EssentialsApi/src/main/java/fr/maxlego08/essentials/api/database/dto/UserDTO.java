package fr.maxlego08.essentials.api.database.dto;

import java.util.UUID;

/**
 * A Data Transfer Object (DTO) representing a user.
 * This class encapsulates the data related to a user, including their unique identifier, name, last location,
 * ban sanction ID, and mute sanction ID.
 */
public record UserDTO(UUID unique_id, String name, String last_location, Integer ban_sanction_id,
                      Integer mute_sanction_id) {
}
