package fr.maxlego08.essentials.api.scoreboard;

import fr.maxlego08.menu.api.requirement.Permissible;

import java.util.List;

public record TaskCondition(String scoreboard, List<Permissible> permissibles) {
}
