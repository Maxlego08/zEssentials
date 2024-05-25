package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.Material;

public class BlockHologramConfiguration extends HologramConfiguration {

    private Material material = Material.GRASS_BLOCK;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
