package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ButtonVaultOpen extends ZButton {

    private final EssentialsPlugin plugin;
    private final int vaultId;

    public ButtonVaultOpen(Plugin plugin, int vaultId) {
        this.plugin = (EssentialsPlugin) plugin;
        this.vaultId = vaultId;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        PlayerVaults playerVaults = plugin.getVaultManager().getPlayerVaults(player);
        Vault targetVault = playerVaults.getTargetVault();

        Vault vault = playerVaults.getVault(this.vaultId);

        var itemstack = this.getItemStack();
        Placeholders placeholders = new Placeholders();
        var vaultManager = plugin.getVaultManager();

        var vaultItemStack = vault.getIconItemStack();
        var icon = vaultItemStack != null ? vaultItemStack.getType().name() : targetVault.getVaultId() == this.vaultId ? vaultManager.getIconOpen() : vaultManager.getIconClose();
        var modelId = vaultItemStack != null && vaultItemStack.hasItemMeta() && vaultItemStack.getItemMeta().hasCustomModelData() ? vaultItemStack.getItemMeta().getCustomModelData() : targetVault.getVaultId() == this.vaultId ? vaultManager.getIconOpenModelId() : vaultManager.getIconCloseModelId();

        placeholders.register("vault-icon", icon);
        placeholders.register("vault-model-id", String.valueOf(modelId));

        placeholders.register("vault-name", vault.getName());
        placeholders.register("vault-id", String.valueOf(this.vaultId));

        return itemstack.build(player, false, placeholders);
    }

    @Override
    public void onLeftClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onLeftClick(player, event, inventory, slot);

        PlayerVaults playerVaults = plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null || vault.getVaultId() == this.vaultId) return;

        this.plugin.getVaultManager().openVault(player, this.vaultId);
    }

    @Override
    public void onRightClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot) {
        super.onRightClick(player, event, inventory, slot);

        this.plugin.getVaultManager().openConfiguration(player, this.vaultId);
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        return this.plugin.getVaultManager().hasPermission(player.getUniqueId(), this.vaultId);
    }
}
