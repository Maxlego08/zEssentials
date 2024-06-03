package fr.maxlego08.essentials.api.scoreboard;

import fr.maxlego08.essentials.api.modules.NonLoadable;
import fr.maxlego08.menu.api.requirement.Permissible;

import java.util.List;

public record JoinCondition(int priority, String scoreboard, List<Permissible> permissibles) implements NonLoadable {
}
