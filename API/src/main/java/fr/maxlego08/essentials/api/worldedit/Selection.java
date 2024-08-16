package fr.maxlego08.essentials.api.worldedit;

import org.bukkit.Location;

public interface Selection {

    Location getFirstLocation();

    void setFirstLocation(Location location);

    Location getSecondLocation();

    void setSecondLocation(Location location);

    boolean isValid();

    Cuboid getCuboid();
}
