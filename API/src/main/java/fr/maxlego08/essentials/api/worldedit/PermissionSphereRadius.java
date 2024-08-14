package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.modules.Loadable;

public record PermissionSphereRadius(String permission, int radius) implements Loadable {
}
