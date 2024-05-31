package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.modules.Loadable;

public record ChatCooldown(long messages, long cooldown) implements Loadable {
}
