package fr.maxlego08.essentials.module.modules.kit;

import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.menu.api.MenuItemStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class KitInventoryHolder implements InventoryHolder {

    private final Player player;
    private final Kit kit;
    private final Inventory inventory;

    public KitInventoryHolder(Player player, Kit kit) {
        this.player = player;
        this.kit = kit;
        this.inventory = Bukkit.createInventory(this, 54, "Kit Editor: " + kit.getName());

        for (int index = 0; index != Math.min(54, kit.getMenuItemStacks().size()); index++) {
            MenuItemStack menuItemStack = kit.getMenuItemStacks().get(index);
            inventory.setItem(index, menuItemStack.build(player, false));
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public Kit getKit() {
        return kit;
    }
}
