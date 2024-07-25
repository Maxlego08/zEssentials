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

        var icon = vault.getIconItemStack() != null ? vault.getIconItemStack().getType().name() : targetVault.getVaultId() == this.vaultId ? vaultManager.getIconOpen() : vaultManager.getIconClose();
        placeholders.register("vault-icon", icon);

        placeholders.register("vault-name", vault.getName());
        placeholders.register("vault-id", String.valueOf(this.vaultId));

        return itemstack.build(player, false, placeholders);
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        PlayerVaults playerVaults = plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null || vault.getVaultId() == this.vaultId) return;

        this.plugin.getVaultManager().openVault(player, this.vaultId);
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
