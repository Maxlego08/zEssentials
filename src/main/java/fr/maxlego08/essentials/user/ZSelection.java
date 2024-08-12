package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.Selection;
import org.bukkit.Location;

public class ZSelection implements Selection {

    private Location firstLocation;
    private Location secondLocation;

    @Override
    public Location getFirstLocation() {
        return firstLocation;
    }

    @Override
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    @Override
    public Location getSecondLocation() {
        return secondLocation;
    }

    @Override
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    @Override
    public boolean isValid() {
        return this.firstLocation != null && this.secondLocation != null;
    }

    @Override
    public Cuboid getCuboid() {
        return new Cuboid(this.firstLocation, this.secondLocation);
    }
}
