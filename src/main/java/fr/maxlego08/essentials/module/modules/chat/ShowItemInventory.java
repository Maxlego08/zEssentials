package fr.maxlego08.essentials.module.modules.chat;

import fr.maxlego08.essentials.api.chat.ShowItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ShowItemInventory implements InventoryHolder {

    private final Inventory inventory;

    public ShowItemInventory(ShowItem showItem, Player player) {
        this.inventory = Bukkit.createInventory(this, InventoryType.DISPENSER);
        this.inventory.setItem(4, showItem.itemStack());
        player.openInventory(inventory);
    }


    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
