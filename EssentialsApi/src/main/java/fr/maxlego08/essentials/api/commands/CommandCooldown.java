package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.modules.Loadable;

public record CommandCooldown(String command, int cooldown) implements Loadable {
}
