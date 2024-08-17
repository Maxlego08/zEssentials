package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.modules.Loadable;

public record PermissionSlotsVault(String permission, int slots) implements Loadable {
}
