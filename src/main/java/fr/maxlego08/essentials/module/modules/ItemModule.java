package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.MenuItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
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

        this.items.clear();
        YamlConfiguration configuration = getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("custom-items");
        if (configurationSection == null) return;

        for (String itemName : configurationSection.getKeys(false)) {
            MenuItemStack menuItemStack = this.plugin.getInventoryManager().loadItemStack(configuration, "custom-items." + itemName + ".", new File(getFolder(), "config.yml"));
            this.items.put(itemName, menuItemStack);
        }
    }

    public Map<String, MenuItemStack> getItems() {
        return this.items;
    }

    public Set<String> getItemsName() {
        return this.items.keySet();
    }

    public boolean isItem(String itemName) {

        return this.items.containsKey(itemName) || this.getMaterial(itemName) != null;
    }

    public @Nullable ItemStack getItemStack(String itemName, OfflinePlayer playerPlayer) {

        if (this.items.containsKey(itemName)) {
            return this.items.get(itemName).build(playerPlayer.isOnline() ? playerPlayer.getPlayer() : null, false);
        }

        Material material = this.getMaterial(itemName);
        return material == null ? null : new ItemStack(material);
    }

    private @Nullable Material getMaterial(String itemName) {
        if (itemName == null) return null;

        NamespacedKey key = NamespacedKey.fromString(itemName.toLowerCase(Locale.ROOT));
        if (key == null || !key.getNamespace().equals(NamespacedKey.MINECRAFT)) return null;

        Material material = Material.getMaterial(key.getKey().toUpperCase(Locale.ROOT));
        return material != null && !material.isLegacy() && material.isItem() && !material.isAir() ? material : null;
    }

    public void give(CommandSender sender, Player player, String itemName, int amount) {

        ItemStack itemStack = this.getItemStack(itemName, player);
        if (itemStack == null) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return;
        }

        String displayName = getItemName(itemStack);
        itemStack.setAmount(Math.max(1, amount));
        this.plugin.give(player, itemStack);

        message(sender, Message.COMMAND_GIVE, "%item%", displayName, "%player%", player.getName(), "%amount%", amount);
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

        message(sender, Message.COMMAND_GIVE, "%item%", getItemName(itemStack), "%player%", player.getName(), "%amount%", amount);
    }

    public void giveAll(CommandSender sender, String itemName, int amount) {

        if (!this.isItem(itemName)) {
            message(sender, Message.COMMAND_GIVE_ERROR, "%item%", itemName);
            return;
        }

        // ToDo, rework for all server give all
        String displayName = null;
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack itemStack = this.getItemStack(itemName, player);
            if (itemStack == null) continue;
            if (displayName == null) displayName = getItemName(itemStack);

            itemStack.setAmount(Math.max(1, amount));
            this.plugin.give(player, itemStack);
        }

        message(sender, Message.COMMAND_GIVE_ALL, "%item%", displayName == null ? itemName : displayName, "%amount%", amount);
    }
}
