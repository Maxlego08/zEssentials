package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class ButtonWarp extends Button {

    private final EssentialsPlugin plugin;
    private final String warpName;

    public ButtonWarp(EssentialsPlugin plugin, String warpName) {
        this.plugin = plugin;
        this.warpName = warpName;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        WarpModule module = this.plugin.getModuleManager().getModule(WarpModule.class);
        User user = this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
        if (user == null) return;

        module.teleport(user, this.warpName);
    }

    @Override
    public boolean closeInventory() {
        return true;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        Optional<Warp> optional = plugin.getWarp(this.warpName);
        return super.checkPermission(player, inventory, placeholders) && optional.map(warp -> warp.hasPermission(player)).orElse(false);
    }
}
