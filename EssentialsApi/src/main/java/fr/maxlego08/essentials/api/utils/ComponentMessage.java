package fr.maxlego08.essentials.api.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface ComponentMessage {

    /**
     * Sends a message to the specified {@link CommandSender}.
     *
     * @param sender  The CommandSender to whom the message will be sent.
     * @param message The message to be sent.
     */
    void sendMessage(CommandSender sender, String message);

    void sendActionBar(Player player, String message);

    Inventory createInventory(String message, int size, InventoryHolder inventoryHolder);
}
