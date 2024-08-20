package fr.maxlego08.essentials.zutils.utils.spigot;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import fr.maxlego08.essentials.api.messages.messages.TitleMessage;
import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void addToLore(ItemStack itemStack, List<String> lore, Placeholders placeholders) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> currentLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
        currentLore.addAll(lore.stream().map(placeholders::parse).toList());
        itemMeta.setLore(currentLore);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void sendBossBar(EssentialsPlugin plugin, Player player, BossBarMessage bossBarMessage) {

    }

    @Override
    public void sendTitle(Player player, TitleMessage titleMessage, Object... objects) {
        player.sendTitle(titleMessage.title(), titleMessage.subtitle(), (int) titleMessage.start(), (int) titleMessage.time(), (int) titleMessage.end());
    }
}
