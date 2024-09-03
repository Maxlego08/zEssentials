package fr.maxlego08.essentials.api.utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record EnderChestHolder(Player player) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        Inventory enderChestInventory = player.getEnderChest();
        Inventory newInventory = Bukkit.createInventory(this, 27, enderChestInventory.getType().defaultTitle());
        int slot = 0;
        for (ItemStack content : enderChestInventory.getContents()) newInventory.setItem(slot++, content);
        return newInventory;
    }
}
