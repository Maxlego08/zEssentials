package fr.maxlego08.essentials.api.scoreboard;

import fr.maxlego08.essentials.api.modules.Loadable;
import fr.maxlego08.essentials.api.modules.NonLoadable;
import fr.maxlego08.menu.api.requirement.Permissible;

import java.util.List;
import java.util.Map;

public record TaskCondition(String scoreboard, List<Permissible> permissibles) implements NonLoadable {
}
