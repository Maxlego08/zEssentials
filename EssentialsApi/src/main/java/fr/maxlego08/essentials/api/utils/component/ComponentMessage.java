package fr.maxlego08.essentials.api.utils.component;

import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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

    void addToLore(ItemStack itemStack, List<String> lore, Placeholders placeholders);
}
