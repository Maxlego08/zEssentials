package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultNoPermission;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ButtonVaultNoPermissionLoader implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonVaultNoPermissionLoader(EssentialsPlugin plugin) {
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
        int vaultId = Integer.parseInt(configuration.getString(path + "vault", "1"));
        return new ButtonVaultNoPermission(this.plugin, vaultId);
    }
}
