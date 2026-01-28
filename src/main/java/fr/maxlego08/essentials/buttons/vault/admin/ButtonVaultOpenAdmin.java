package fr.maxlego08.essentials.buttons.vault.admin;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ButtonVaultOpenAdmin extends Button {

    private final EssentialsPlugin plugin;
    private final int vaultId;

    public ButtonVaultOpenAdmin(Plugin plugin, int vaultId) {
        this.plugin = (EssentialsPlugin) plugin;
        this.vaultId = vaultId;
    }

    @Override
    public ItemStack getCustomItemStack(Player player, Placeholders placeholders) {
        PlayerVaults viewerVaults = this.plugin.getVaultManager().getPlayerVaults(player);
        PlayerVaults targetVaults = viewerVaults.getTargetPlayerVaults();
        if (targetVaults == null) return this.getItemStack().build(player, false, new Placeholders());

        Vault targetVault = targetVaults.getVault(this.vaultId);
        Vault currentVault = viewerVaults.getTargetVault();

        var itemstack = this.getItemStack();
        var vaultManager = plugin.getVaultManager();

        var vaultItemStack = targetVault.getIconItemStack();
        var icon = vaultItemStack != null ? vaultItemStack.getType().name() : currentVault.getVaultId() == this.vaultId ? vaultManager.getIconOpen() : vaultManager.getIconClose();
        var modelId = vaultItemStack != null && vaultItemStack.hasItemMeta() && vaultItemStack.getItemMeta().hasCustomModelData() ? vaultItemStack.getItemMeta().getCustomModelData() : currentVault.getVaultId() == this.vaultId ? vaultManager.getIconOpenModelId() : vaultManager.getIconCloseModelId();

        placeholders.register("vault-icon", icon);
        placeholders.register("vault-model-id", String.valueOf(modelId));
        placeholders.register("vault-name", targetVault.getName());
        placeholders.register("vault-id", String.valueOf(this.vaultId));

        return itemstack.build(player, false, placeholders);
    }

    @Override
    public void onLeftClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onLeftClick(player, event, inventory, slot);

        PlayerVaults viewerVaults = plugin.getVaultManager().getPlayerVaults(player);
        PlayerVaults targetVaults = viewerVaults.getTargetPlayerVaults();
        if (targetVaults == null) return;

        Vault vault = viewerVaults.getTargetVault();
        if (vault == null || vault.getVaultId() == this.vaultId) return;

        this.plugin.getVaultManager().openVault(player, Bukkit.getOfflinePlayer(targetVaults.getUniqueId()), this.vaultId);
    }

    @Override
    public void onRightClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot) {
        super.onRightClick(player, event, inventory, slot);

        // ToDo
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        PlayerVaults viewerVaults = this.plugin.getVaultManager().getPlayerVaults(player);
        PlayerVaults targetVaults = viewerVaults.getTargetPlayerVaults();
        return targetVaults != null && this.plugin.getVaultManager().hasPermission(targetVaults.getUniqueId(), this.vaultId);
    }
}

