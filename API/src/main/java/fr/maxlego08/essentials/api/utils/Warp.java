package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.permissions.Permissible;

import java.util.Collection;
import java.util.Locale;

/**
 * Represents a warp location.
 * This record encapsulates data related to a warp, including its name and location.
 */
public record Warp(String name, SafeLocation location) {

    /**
     * Checks if the specified permissible entity has permission to warp to this location.
     *
     * @param permissible The permissible entity (e.g., player or command sender).
     * @return true if the permissible entity has permission, false otherwise.
     */
    public boolean hasPermission(Permissible permissible) {
        if (permissible.hasPermission(Permission.ESSENTIALS_WARP.asPermission())) {
            return true;
        }
        return permissible.hasPermission(Permission.ESSENTIALS_WARP_.asPermission(this.name.toLowerCase(Locale.ROOT)));
    }

    /**
     * Checks if the permissible can use at least one warp or the global warp permission.
     */
    public static boolean canAccessAnyWarp(Permissible permissible, Collection<Warp> warps) {
        if (permissible.hasPermission(Permission.ESSENTIALS_WARP.asPermission())) {
            return true;
        }
        for (Warp warp : warps) {
            if (warp.hasPermission(permissible)) {
                return true;
            }
        }
        return false;
    }
}
