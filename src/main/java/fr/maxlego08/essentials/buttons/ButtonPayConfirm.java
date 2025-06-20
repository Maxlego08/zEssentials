package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ButtonPayConfirm extends Button {

    private final EssentialsPlugin plugin;

    public ButtonPayConfirm(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);
        User user = this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
        EconomyManager economyManager = plugin.getEconomyManager();
        User targetUser = user.getTargetUser();
        economyManager.pay(player.getUniqueId(), player.getName(), targetUser.getUniqueId(), targetUser.getName(), user.getTargetEconomy(), user.getTargetDecimal());
    }
}
