package fr.maxlego08.essentials.api.home;

import fr.maxlego08.essentials.api.modules.Loadable;

/**
 * Represents a permission record for accessing home locations, specifying the permission and the amount.
 */
public record HomePermission(String permission, int amount) implements Loadable {
}

