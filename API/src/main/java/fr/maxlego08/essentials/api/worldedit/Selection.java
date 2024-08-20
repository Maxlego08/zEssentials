package fr.maxlego08.essentials.api.worldedit;

import org.bukkit.Location;

/**
 * Interface representing a selection of two locations in a Minecraft world.
 * The selection can be used to define an area or region, typically for operations like WorldEdit.
 */
public interface Selection {

    /**
     * Gets the first location of the selection.
     *
     * @return the first {@link Location} of the selection
     */
    Location getFirstLocation();

    /**
     * Sets the first location of the selection.
     *
     * @param location the first {@link Location} to set
     */
    void setFirstLocation(Location location);

    /**
     * Gets the second location of the selection.
     *
     * @return the second {@link Location} of the selection
     */
    Location getSecondLocation();

    /**
     * Sets the second location of the selection.
     *
     * @param location the second {@link Location} to set
     */
    void setSecondLocation(Location location);

    /**
     * Checks if the selection is valid. A selection is considered valid if both
     * the first and second locations are set and are in the same world.
     *
     * @return true if the selection is valid, false otherwise
     */
    boolean isValid();

    /**
     * Gets the cuboid region defined by the selection.
     * A cuboid is a three-dimensional rectangular area.
     *
     * @return the {@link Cuboid} representing the selected area
     */
    Cuboid getCuboid();
}
