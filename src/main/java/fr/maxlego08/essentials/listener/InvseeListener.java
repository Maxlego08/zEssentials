package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.utils.inventory.EnderChestHolder;
import fr.maxlego08.essentials.api.utils.inventory.PlayerInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InvseeListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof EnderChestHolder enderChestHolder) {
            var targetPlayer = enderChestHolder.player();
            int slot = 0;
            for (ItemStack content : event.getInventory().getContents()) {
                targetPlayer.getEnderChest().setItem(slot++, content);
            }
            targetPlayer.saveData();
        } else if (event.getInventory().getHolder() instanceof PlayerInventoryHolder playerInventoryHolder) {
            var targetPlayer = playerInventoryHolder.player();
            for (int slot = 0; slot != 36; slot++) {
                targetPlayer.getInventory().setItem(slot++, event.getInventory().getItem(slot));
            }
            targetPlayer.saveData();
        }
    }
}
