package fr.maxlego08.essentials.buttons.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

public class ButtonSanctionInformation extends ZButton {

    private final EssentialsPlugin plugin;

    public ButtonSanctionInformation(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return super.getCustomItemStack(player);

        User targetuser = user.getTargetUser();
        if (targetuser == null) return super.getCustomItemStack(player);

        MenuItemStack menuItemStack = this.getItemStack();
        Placeholders placeholders = new Placeholders();

        placeholders.register("target", targetuser.getName());
        placeholders.register("is_ban", String.valueOf(targetuser.getOption(Option.BAN)));
        placeholders.register("is_mute", String.valueOf(targetuser.getOption(Option.MUTE)));

        ItemStack itemStack = menuItemStack.build(player, false, placeholders);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetuser.getUniqueId());
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        this.plugin.openInventory(player, event.getClick().isLeftClick() ? "sanctions" : "sanction_history");
    }
}
