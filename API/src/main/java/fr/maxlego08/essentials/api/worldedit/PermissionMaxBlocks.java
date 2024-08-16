package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.modules.Loadable;

public record PermissionMaxBlocks(String permission, int blocks) implements Loadable {
}
