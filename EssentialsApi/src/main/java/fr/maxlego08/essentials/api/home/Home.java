package fr.maxlego08.essentials.api.home;

import org.bukkit.Location;
import org.bukkit.Material;

public interface Home {

    Location getLocation();

    String getName();

    Material getMaterial();

    void setMaterial(Material material);

}
