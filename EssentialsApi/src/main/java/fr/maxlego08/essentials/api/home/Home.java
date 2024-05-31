package fr.maxlego08.essentials.api.home;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Represents a home location in the plugin system.
 */
public interface Home {

    /**
     * Gets the location of the home.
     *
     * @return The location of the home.
     */
    Location getLocation();

    /**
     * Gets the name of the home.
     *
     * @return The name of the home.
     */
    String getName();

    /**
     * Gets the material associated with the home.
     *
     * @return The material associated with the home.
     */
    Material getMaterial();

    /**
     * Sets the material associated with the home.
     *
     * @param material The material to set for the home.
     */
    void setMaterial(Material material);

}

