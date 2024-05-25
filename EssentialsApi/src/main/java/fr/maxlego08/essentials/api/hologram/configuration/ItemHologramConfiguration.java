package fr.maxlego08.essentials.api.hologram.configuration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemHologramConfiguration extends HologramConfiguration {

    private ItemStack itemStack = new ItemStack(Material.DIAMOND);

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
