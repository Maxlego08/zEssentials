package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.modules.Loadable;
import fr.maxlego08.essentials.api.worldedit.Cuboid;

import java.util.List;

/**
 * Represents a restriction applied to a command. A restriction can prevent a
 * command from being executed in specific worlds or inside configured cuboids.
 *
 * @param commands         Commands name without leading slash.
 * @param bypassPermission Permission allowing players to bypass this restriction.
 * @param worlds           List of world names where the command is disabled.
 * @param cuboids          List of cuboids where the command is disabled.
 */
public record CommandRestriction(List<String> commands, String bypassPermission, List<String> worlds,
                                 List<Cuboid> cuboids) implements Loadable {
}
