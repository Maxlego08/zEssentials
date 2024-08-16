package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemModule extends ZModule {

    private final Map<String, MenuItemStack> items = new HashMap<>();

    public ItemModule(ZEssentialsPlugin plugin) {
        super(plugin, "items");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
        YamlConfiguration configuration = getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("custom-items");
        if (configurationSection == null) return;

        for (String itemName : configurationSection.getKeys(false)) {
            try {
                MenuItemStack menuItemStack = loader.load(configuration, "custom-items." + itemName + ".", new File(getFolder(), "config.yml"));
                this.items.put(itemName, menuItemStack);
            } catch (InventoryException exception) {
                exception.printStackTrace();
            }
        }
    }

    public Map<String, MenuItemStack> getItems() {
        return this.items;
    }

    public Set<String> getItemsName() {
        return this.items.keySet();
    }

    public boolean isItem(String itemName) {

        if (this.items.containsKey(itemName)) return true;

        try {
            Material.valueOf(itemName.toUpperCase());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public @Nullable ItemStack getItemStack(String itemName, OfflinePlayer playerPlayer) {

        if (this.items.containsKey(itemName)) {
            return this.items.get(itemName).build(playerPlayer.isOnline() ? playerPlayer.getPlayer() : null, false);
        }

        try {

            Material material = Material.valueOf(itemName.toUpperCase());
            return new ItemStack(material);

        } catch (Exception exception) {
            return null;
        }
    }

    public void give(CommandSender sender, Player player, String itemName, int amount) {

        ItemStack itemStack = this.getItemStack(itemName, player);
        if (itemStack == null) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return;
        }

        itemStack.setAmount(Math.max(1, amount));
        this.plugin.give(player, itemStack);

        message(sender, Message.COMMAND_GIVE, "%item%", itemName, "%player%", player.getName(), "%amount%", amount);
    }

    public void giveFullInventory(CommandSender sender, Player player, String itemName) {

        ItemStack itemStack = this.getItemStack(itemName, player);
        if (itemStack == null) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return;
        }

        itemStack.setAmount(itemStack.getMaxStackSize());
        int amount = 0;
        var inventory = player.getInventory();
        for (int slot = 0; slot != 36; slot++) {
            if (inventory.getContents()[slot] == null || inventory.getContents()[slot].getType().isAir()) {
                inventory.setItem(slot, itemStack.clone());
                amount += itemStack.getAmount();
            }
        }

        message(sender, Message.COMMAND_GIVE, "%item%", itemName, "%player%", player.getName(), "%amount%", amount);
    }

    public void giveAll(CommandSender sender, String itemName, int amount) {

        if (!this.isItem(itemName)) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return;
        }

        // ToDo, rework for all server give all
        Bukkit.getOnlinePlayers().forEach(player -> {
            ItemStack itemStack = this.getItemStack(itemName, player);
            if (itemStack == null) return;

            itemStack.setAmount(Math.max(1, amount));
            this.plugin.give(player, itemStack);
        });

        message(sender, Message.COMMAND_GIVE_ALL, "%item%", itemName, "%amount%", amount);
    }
}
