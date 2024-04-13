package fr.maxlego08.essentials.user;

import fr.maxlego08.essentials.api.home.Home;
import org.bukkit.Location;

public class ZHome implements Home {

    private final Location location;
    private final String name;

    public ZHome(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public String getName() {
        return name;
    }
}
