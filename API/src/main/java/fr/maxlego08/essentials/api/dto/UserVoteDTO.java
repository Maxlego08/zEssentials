package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

/**
 * A Data Transfer Object (DTO) representing a user.
 * This class encapsulates the data related to a user, including their unique identifier, name, last location,
 * ban sanction ID, and mute sanction ID.
 */
public record UserVoteDTO(UUID unique_id, long vote, long vote_offline) {
}
