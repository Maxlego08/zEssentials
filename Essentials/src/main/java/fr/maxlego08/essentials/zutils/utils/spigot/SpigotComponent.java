package fr.maxlego08.essentials.zutils.utils.spigot;

import fr.maxlego08.essentials.api.utils.ComponentMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpigotComponent implements ComponentMessage {

    @Override
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.sendActionBar(message);
    }

    @Override
    public Inventory createInventory(String message, int size, InventoryHolder inventoryHolder) {
        return Bukkit.createInventory(inventoryHolder, size, message);
    }
}
