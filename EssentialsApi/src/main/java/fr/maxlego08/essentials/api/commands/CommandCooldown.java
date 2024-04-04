package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.util.List;
import java.util.Map;

public record CommandCooldown(String command, int cooldown, List<Map<String, Object>> permissions) implements Loadable {
}
