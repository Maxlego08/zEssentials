package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.permissions.Permissible;

/**
 * Represents a warp location.
 * This record encapsulates data related to a warp, including its name and location.
 */
public record Warp(String name, Location location) {

    /**
     * Checks if the specified permissible entity has permission to warp to this location.
     *
     * @param permissible The permissible entity (e.g., player or command sender).
     * @return true if the permissible entity has permission, false otherwise.
     */
    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(Permission.ESSENTIALS_WARP.asPermission(this.name));
    }
}
