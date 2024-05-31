package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.ButtonWarp;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ButtonWarpLoader implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonWarpLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public Class<? extends Button> getButton() {
        return ButtonWarp.class;
    }

    @Override
    public String getName() {
        return "zessentials_warp";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String warpName = yamlConfiguration.getString(s + "warp");
        return new ButtonWarp(this.plugin, warpName);
    }
}
