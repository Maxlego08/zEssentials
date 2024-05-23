package fr.maxlego08.essentials.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Hologram {

    private final Location location;

    public Hologram(Location location) {
        this.location = location;
    }

    public abstract void create(Player player);

    public abstract void delete(Player player);

    public abstract void update(Player player);

}
