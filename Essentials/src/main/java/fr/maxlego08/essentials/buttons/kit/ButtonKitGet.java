package fr.maxlego08.essentials.buttons.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ButtonKitGet extends ZButton {

    private final EssentialsPlugin plugin;
    private final String kitName;

    public ButtonKitGet(EssentialsPlugin plugin, String kitName) {
        this.plugin = plugin;
        this.kitName = kitName;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        Optional<Kit> optional = this.plugin.getKit(this.kitName);
        if (optional.isEmpty()) return;

        Kit kit = optional.get();

        if (event.getClick().isLeftClick()) {
            this.plugin.giveKit(user, kit, false);
        } else if (event.getClick().isRightClick()) {
            user.openKitPreview(kit);
        }

        super.onClick(player, event, inventory, slot, placeholders);
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        Placeholders placeholders = new Placeholders();

        Optional<Kit> optional = this.plugin.getKit(this.kitName);
        if (optional.isEmpty()) return super.getCustomItemStack(player);
        Kit kit = optional.get();

        placeholders.register("cooldown", TimerBuilder.getStringTime(kit.getCooldown() * 1000));
        return this.getItemStack().build(player, false, placeholders);
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return false;
        Optional<Kit> optional = this.plugin.getKit(this.kitName);
        return optional.filter(kit -> super.checkPermission(player, inventory, placeholders) && kit.hasPermission(player)).isPresent();
    }
}
