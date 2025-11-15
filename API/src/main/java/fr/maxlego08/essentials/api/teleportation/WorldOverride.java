package fr.maxlego08.essentials.api.teleportation;

import fr.maxlego08.essentials.api.modules.Loadable;

public record WorldOverride(String from, String to) implements Loadable {
}
