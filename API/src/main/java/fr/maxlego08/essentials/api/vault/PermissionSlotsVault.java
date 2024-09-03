package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.modules.Loadable;

/**
 * A record representing a mapping between a permission and the number of slots in a vault.
 * This is used to define how many slots a player has access to based on their permissions.
 *
 * @param permission the permission string associated with the vault slots
 * @param slots      the number of slots available in the vault for this permission
 */
public record PermissionSlotsVault(String permission, int slots) implements Loadable {
}
