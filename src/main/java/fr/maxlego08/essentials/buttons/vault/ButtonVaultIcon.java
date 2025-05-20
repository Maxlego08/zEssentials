package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ButtonVaultIcon extends Button {

    private final EssentialsPlugin plugin;

    public ButtonVaultIcon(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        PlayerVaults playerVaults = plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return super.getCustomItemStack(player);

        var itemstack = this.getItemStack();
        Placeholders placeholders = new Placeholders();
        var vaultManager = plugin.getVaultManager();

        var vaultItemStack = vault.getIconItemStack();
        var icon = vaultItemStack != null ? vaultItemStack.getType().name() : vaultManager.getIconOpen();
        var modelId = vaultItemStack != null && vaultItemStack.hasItemMeta() && vaultItemStack.getItemMeta().hasCustomModelData() ? vaultItemStack.getItemMeta().getCustomModelData() : vaultManager.getIconOpenModelId();

        placeholders.register("vault-icon", icon);
        placeholders.register("vault-model-id", String.valueOf(modelId));

        return itemstack.build(player, false, placeholders);
    }

    @Override
    public void onLeftClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onLeftClick(player, event, inventory, slot);

        var vaultManager = this.plugin.getVaultManager();
        PlayerVaults playerVaults = vaultManager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        vaultManager.changeIcon(player, vault);
    }

    @Override
    public void onRightClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onRightClick(player, event, inventory, slot);

        var vaultManager = this.plugin.getVaultManager();
        PlayerVaults playerVaults = vaultManager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        vaultManager.resetIcon(player, vault);
    }
}
