package fr.maxlego08.essentials.api.dto;

import java.util.Date;
import java.util.UUID;

public record VoteSiteDTO(UUID player_id, String site, Date last_vote_at) {
}
