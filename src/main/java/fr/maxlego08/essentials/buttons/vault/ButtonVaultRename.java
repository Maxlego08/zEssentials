package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ButtonVaultRename extends Button {

    private final EssentialsPlugin plugin;

    public ButtonVaultRename(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public void onLeftClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onLeftClick(player, event, inventory, slot);

        var vaultManager = this.plugin.getVaultManager();
        PlayerVaults playerVaults = vaultManager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        vaultManager.changeName(player, vault);
    }

    @Override
    public void onRightClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onRightClick(player, event, inventory, slot);

        var vaultManager = this.plugin.getVaultManager();
        PlayerVaults playerVaults = vaultManager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        vaultManager.resetName(player, vault);
    }
}
