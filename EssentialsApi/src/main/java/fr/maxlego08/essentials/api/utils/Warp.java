package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.permissions.Permissible;

public record Warp(String name, Location location) {

    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(Permission.ESSENTIALS_WARP.asPermission(this.name));
    }
}
