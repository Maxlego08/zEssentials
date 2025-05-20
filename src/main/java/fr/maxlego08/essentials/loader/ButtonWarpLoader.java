package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.ButtonWarp;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonWarpLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonWarpLoader(EssentialsPlugin plugin) {
        super(plugin, "zessentials_warp");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String warpName = yamlConfiguration.getString(s + "warp");
        return new ButtonWarp(this.plugin, warpName);
    }
}
