package fr.maxlego08.essentials.api.home;

import fr.maxlego08.essentials.api.modules.Loadable;

public record HomePermission(String permission, int amount) implements Loadable {
}
