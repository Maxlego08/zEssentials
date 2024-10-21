package fr.maxlego08.essentials.api.vote;

import fr.maxlego08.menu.api.requirement.Action;

import java.util.List;

public record VotePartyReward(List<Action> actions, String permissions, List<Action> permissionActions, List<String> globalCommands) {
}
