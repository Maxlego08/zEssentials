package fr.maxlego08.essentials.api.utils.component;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Interface for handling various types of component-based messages and interactions within the server.
 */
public interface ComponentMessage {

    /**
     * Sends a text message to a specified CommandSender, which could be a player or the console.
     *
     * @param sender  the recipient of the message
     * @param message the message to be sent
     */
    void sendMessage(CommandSender sender, String message);

    /**
     * Sends an action bar message to a specified player.
     *
     * @param player  the player who will receive the action bar message
     * @param message the message to display on the action bar
     */
    void sendActionBar(Player player, String message);

    /**
     * Creates a new inventory with a custom name and size, associated with a specific InventoryHolder.
     *
     * @param message         the name/title of the inventory
     * @param size            the size of the inventory in slots (must be a multiple of 9)
     * @param inventoryHolder the holder of the inventory, usually the object or entity that owns the inventory
     * @return the created Inventory instance
     */
    Inventory createInventory(String message, int size, InventoryHolder inventoryHolder);

    /**
     * Adds a list of lore lines to an ItemStack's existing lore, with support for placeholders.
     *
     * @param itemStack    the ItemStack to which the lore will be added
     * @param lore         the list of lore lines to add
     * @param placeholders the placeholders to replace in the lore lines
     */
    void addToLore(ItemStack itemStack, List<String> lore, Placeholders placeholders);

    /**
     * Sends a title message to a specified player, with options for the main title, subtitle, and fade timings.
     *
     * @param player       the player who will receive the title message
     * @param titleMessage the TitleMessage object containing the title, subtitle, and timing information
     * @param args The arguments
     */
    void sendTitle(Player player, TitleMessage titleMessage, Object... args);

    /**
     * Sends a boss bar message to a specified player, which can be used to display progress or important information.
     *
     * @param plugin         the plugin instance responsible for sending the boss bar
     * @param player         the player who will receive the boss bar message
     * @param bossBarMessage the BossBarMessage object containing the details of the boss bar
     */
    void sendBossBar(EssentialsPlugin plugin, Player player, BossBarMessage bossBarMessage);
}

