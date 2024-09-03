package fr.maxlego08.essentials.api.utils.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record PlayerInventoryHolder(Player player) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        Inventory playerInventory = player.getInventory();
        Inventory newInventory = Bukkit.createInventory(this, 36, playerInventory.getType().defaultTitle());
        for (int slot = 0; slot != 36; slot++) newInventory.setItem(slot++, playerInventory.getItem(slot));
        return newInventory;
    }
}
