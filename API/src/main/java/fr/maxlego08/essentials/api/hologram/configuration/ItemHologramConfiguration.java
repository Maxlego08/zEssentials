package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This class represents the configuration settings for an item hologram.
 * It extends the {@link HologramConfiguration} class to include additional properties
 * specific to item holograms, such as the {@link ItemStack} to be displayed.
 */
public class ItemHologramConfiguration extends HologramConfiguration {

    private ItemStack itemStack = new ItemStack(Material.DIAMOND);

    /**
     * Gets the {@link ItemStack} that is currently set for this hologram.
     *
     * @return the current {@link ItemStack} being displayed
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Sets the {@link ItemStack} to be displayed by this hologram.
     *
     * @param itemStack the new {@link ItemStack} to display
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}

