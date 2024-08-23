package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.utils.inventory.PlayerHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InvseeListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof PlayerHolder playerHolder) {
            var targetPlayer = playerHolder.player();
            int slot = 0;
            for (ItemStack content : event.getInventory().getContents()) {
                targetPlayer.getEnderChest().setItem(slot++, content);
            }
            targetPlayer.saveData();
        }
    }

}
