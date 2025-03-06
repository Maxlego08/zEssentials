package fr.maxlego08.essentials.api.vote;

import java.util.List;

public record VoteReward(int min, int max, List<String> actions) {
}
