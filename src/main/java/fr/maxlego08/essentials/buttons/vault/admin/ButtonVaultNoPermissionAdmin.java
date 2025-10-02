package fr.maxlego08.essentials.buttons.vault.admin;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonVaultNoPermissionAdmin extends Button {

    private final int vaultId;
    private final EssentialsPlugin plugin;

    public ButtonVaultNoPermissionAdmin(EssentialsPlugin plugin, int vaultId) {
        this.vaultId = vaultId;
        this.plugin = plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        PlayerVaults viewerVaults = plugin.getVaultManager().getPlayerVaults(player);
        PlayerVaults targetVaults = viewerVaults.getTargetPlayerVaults();
        if (targetVaults == null) return this.getItemStack().build(player, false, new Placeholders());
        Vault vault = targetVaults.getVault(this.vaultId);

        var itemstack = this.getItemStack();
        Placeholders placeholders = new Placeholders();

        placeholders.register("vault-name", vault.getName());

        return itemstack.build(player, false, placeholders);
    }
}

