package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.modules.Loadable;

public record PermissionRadius(String permission, int radius) implements Loadable {
}
