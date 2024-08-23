package fr.maxlego08.essentials.api.utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record PlayerInventoryHolder(Player player) implements InventoryHolder, PlayerHolder {

    @Override
    public @NotNull Inventory getInventory() {
        Inventory playerInventory = player.getInventory();
        Inventory newInventory = Bukkit.createInventory(this, 36, playerInventory.getType().defaultTitle());
        int slot = 0;
        for (ItemStack content : playerInventory.getContents()) newInventory.setItem(slot++, content);
        return newInventory;
    }
}
