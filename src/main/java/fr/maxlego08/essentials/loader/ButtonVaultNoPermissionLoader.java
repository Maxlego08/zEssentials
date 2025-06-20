package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultNoPermission;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonVaultNoPermissionLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonVaultNoPermissionLoader(EssentialsPlugin plugin) {
        super(plugin, "ZESSENTIALS_VAULT_NO_PERMISSION");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        int vaultId = Integer.parseInt(configuration.getString(path + "vault", "1"));
        return new ButtonVaultNoPermission(this.plugin, vaultId);
    }
}
