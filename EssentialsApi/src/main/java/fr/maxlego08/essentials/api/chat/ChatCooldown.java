package fr.maxlego08.essentials.api.chat;

import fr.maxlego08.essentials.api.modules.Loadable;

public record ChatCooldown(long messages, long cooldown) implements Loadable {
}
