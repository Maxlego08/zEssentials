package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.util.List;
import java.util.Map;

/**
 * Represents a cooldown configuration for a command.
 * This record encapsulates data related to a command's cooldown, including the command name,
 * cooldown duration, and permissions required.
 *
 * @param command     The name of the command.
 * @param cooldown    The cooldown duration in seconds.
 * @param permissions A list of permissions required to execute the command.
 * @see Loadable
 */
public record CommandCooldown(String command, int cooldown, List<Map<String, Object>> permissions) implements Loadable {
}
