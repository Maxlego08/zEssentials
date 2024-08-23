package fr.maxlego08.essentials.listener;

import fr.maxlego08.essentials.api.utils.EnderChestHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class EnderChestListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof EnderChestHolder enderChestHolder) {
            var targetPlayer = enderChestHolder.player();
            int slot = 0;
            for (ItemStack content : event.getInventory().getContents()) {
                try {
                    targetPlayer.getEnderChest().setItem(slot++, content);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            targetPlayer.saveData();
        }
    }

}
