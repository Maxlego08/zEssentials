package fr.maxlego08.essentials.api.vote;

import java.util.List;

public record VotePartyReward(List<String> commands, String permissions, List<String> commandsPermission) {
}
