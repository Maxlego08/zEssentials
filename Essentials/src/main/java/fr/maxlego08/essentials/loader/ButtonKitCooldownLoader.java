package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.ButtonWarp;
import fr.maxlego08.essentials.buttons.kit.ButtonKitCooldown;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ButtonKitCooldownLoader implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonKitCooldownLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public Class<? extends Button> getButton() {
        return ButtonWarp.class;
    }

    @Override
    public String getName() {
        return "zessentials_kit_cooldown";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String kitName = yamlConfiguration.getString(s + "kit");
        return new ButtonKitCooldown(this.plugin, kitName);
    }
}
