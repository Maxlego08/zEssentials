package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public record WorldEditItem(
        String name,
        long maxUse,
        double priceMultiplier,
        MenuItemStack menuItemStack
) {

    public static final NamespacedKey KEY_WORLDEDIT = new NamespacedKey(JavaPlugin.getProvidingPlugin(EssentialsPlugin.class), "worldedit");
    public static final NamespacedKey KEY_WORLDEDIT_USE = new NamespacedKey(JavaPlugin.getProvidingPlugin(EssentialsPlugin.class), "worldedit-use");


    public ItemStack getItemStack(Player player, long use) {

        Placeholders placeholders = new Placeholders();
        placeholders.register("use", String.valueOf(use));
        ItemStack itemStack = this.menuItemStack.build(player, false, placeholders);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(KEY_WORLDEDIT, PersistentDataType.STRING, this.name);
        persistentDataContainer.set(KEY_WORLDEDIT_USE, PersistentDataType.LONG, use);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
