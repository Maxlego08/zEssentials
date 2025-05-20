package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.buttons.kit.ButtonKitCooldown;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

public class ButtonKitCooldownLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonKitCooldownLoader(EssentialsPlugin plugin) {
        super(plugin, "zessentials_kit_cooldown");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration yamlConfiguration, String s, DefaultButtonValue defaultButtonValue) {
        String kitName = yamlConfiguration.getString(s + "kit");
        return new ButtonKitCooldown(this.plugin, kitName);
    }
}
