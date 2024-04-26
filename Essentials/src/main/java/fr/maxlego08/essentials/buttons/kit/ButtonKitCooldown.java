package fr.maxlego08.essentials.buttons.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class ButtonKitCooldown extends ZButton {

    private final EssentialsPlugin plugin;
    private final String kitName;

    public ButtonKitCooldown(EssentialsPlugin plugin, String kitName) {
        this.plugin = plugin;
        this.kitName = kitName;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        Placeholders placeholders = new Placeholders();

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return super.getCustomItemStack(player);

        Optional<Kit> optional = this.plugin.getKit(this.kitName);
        if (optional.isEmpty()) return super.getCustomItemStack(player);
        Kit kit = optional.get();

        placeholders.register("cooldown", TimerBuilder.getStringTime(user.getKitCooldown(kit) - System.currentTimeMillis()));
        return this.getItemStack().build(player, false, placeholders);
    }

    @Override
    public List<String> buildLore(Player player) {

        User user = this.plugin.getUser(player.getUniqueId());
        Optional<Kit> optional = this.plugin.getKit(this.kitName);
        if (optional.isEmpty() || user == null) return super.buildLore(player);

        Kit kit = optional.get();

        return super.buildLore(player).stream().map(line -> line.replace("%cooldown%", TimerBuilder.getStringTime(user.getKitCooldown(kit) - System.currentTimeMillis()))).toList();
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
        return optional.filter(kit -> super.checkPermission(player, inventory, placeholders) && user.isKitCooldown(kit)).isPresent();
    }
}
