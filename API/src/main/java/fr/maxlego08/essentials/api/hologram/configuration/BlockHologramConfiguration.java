package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.Material;

import org.bukkit.Material;

/**
 * This class represents the configuration settings for a block hologram.
 * It extends the {@link HologramConfiguration} class to include properties
 * specific to block holograms, such as the material of the block.
 */
public class BlockHologramConfiguration extends HologramConfiguration {

    private Material material = Material.GRASS_BLOCK;

    /**
     * Gets the {@link Material} of the block used in the hologram.
     *
     * @return the block material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the {@link Material} of the block used in the hologram.
     *
     * @param material the block material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
}
