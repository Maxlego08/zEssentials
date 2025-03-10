package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.Selection;
import fr.maxlego08.essentials.zutils.utils.cube.CubeDisplay;
import org.bukkit.Color;
import org.bukkit.Location;

public class ZSelection implements Selection {

    private Location firstLocation;
    private Location secondLocation;
    private CubeDisplay cubeDisplay;

    @Override
    public Location getFirstLocation() {
        return firstLocation;
    }

    @Override
    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
        this.createOrUpdate(firstLocation);
    }

    @Override
    public Location getSecondLocation() {
        return secondLocation;
    }

    @Override
    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
        this.createOrUpdate(secondLocation);
    }

    private void createOrUpdate(Location location) {

        if (this.cubeDisplay != null) {
            this.cubeDisplay.remove();
        }

        if (!isValid()) {

            this.cubeDisplay = new CubeDisplay(new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY() + 0.5, location.getBlockZ() + 0.5), 4.01, 4.01, 4.01, Color.fromARGB(80, 250, 40, 40));
            this.cubeDisplay.spawn();

        } else {

            var cuboid = getCuboid();
            location = cuboid.getCenter();

            this.cubeDisplay = new CubeDisplay(location, cuboid.getSizeX() * 4 + 0.1, cuboid.getSizeY() * 4 + 0.01, cuboid.getSizeZ() * 4 + 0.1, Color.fromARGB(80, 40, 250, 40));
            this.cubeDisplay.spawn();
        }
    }

    @Override
    public void reset() {
        if (this.cubeDisplay != null) {
            this.cubeDisplay.remove();
            this.cubeDisplay = null;
        }
    }

    @Override
    public boolean isValid() {
        return this.firstLocation != null && this.secondLocation != null;
    }

    @Override
    public Cuboid getCuboid() {
        return new Cuboid(this.firstLocation, this.secondLocation);
    }

    @Override
    public void cancel() {
        reset();
        this.firstLocation = null;
        this.secondLocation = null;
    }
}
