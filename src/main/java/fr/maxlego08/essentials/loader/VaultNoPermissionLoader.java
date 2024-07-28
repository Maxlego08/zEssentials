package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultNoPermission;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class VaultNoPermissionLoader implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public VaultNoPermissionLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ButtonVaultNoPermission.class;
    }

    @Override
    public String getName() {
        return "ZESSENTIALS_VAULT_NO_PERMISSION";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        int vaultId = configuration.getInt(path + "vault", 1);
        return new ButtonVaultNoPermission(this.plugin, vaultId);
    }
}
