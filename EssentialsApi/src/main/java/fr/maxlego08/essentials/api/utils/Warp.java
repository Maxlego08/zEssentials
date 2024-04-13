package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.permissions.Permissible;

public class Warp {

    private final String name;
    private final Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(Permission.ESSENTIALS_WARP.asPermission(this.name));
    }
}
