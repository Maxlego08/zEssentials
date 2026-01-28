package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonVaultNoPermission extends Button {

    private final int vaultId;
    private final EssentialsPlugin plugin;

    public ButtonVaultNoPermission(EssentialsPlugin plugin, int vaultId) {
        this.vaultId = vaultId;
        this.plugin = plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player, Placeholders placeholders) {

        PlayerVaults playerVaults = plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getVault(this.vaultId);

        var itemstack = this.getItemStack();

        placeholders.register("vault-name", vault.getName());

        return itemstack.build(player, false, placeholders);
    }
}
