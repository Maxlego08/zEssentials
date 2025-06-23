package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.vault.ButtonVaultOpen;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ButtonVaultOpenLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonVaultOpenLoader(EssentialsPlugin plugin) {
        super(plugin, "ZESSENTIALS_VAULT_OPEN");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        int vaultId = Integer.parseInt(configuration.getString(path + "vault", "1"));
        return new ButtonVaultOpen(this.plugin, vaultId);
    }
}
