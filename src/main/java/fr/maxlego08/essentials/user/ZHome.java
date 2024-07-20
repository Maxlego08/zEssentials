package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.home.Home;
import org.bukkit.Location;
import org.bukkit.Material;

public class ZHome implements Home {

    private final Location location;
    private final String name;
    private Material material;

    public ZHome(Location location, String name, Material material) {
        this.location = location;
        this.name = name;
        this.material = material;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }
}
