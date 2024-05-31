package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.buttons.ButtonWarp;
import fr.maxlego08.essentials.buttons.sanction.ButtonSanction;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.time.Duration;

public class ButtonSanctionLoader extends ZUtils implements ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonSanctionLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public Class<? extends Button> getButton() {
        return ButtonSanction.class;
    }

    @Override
    public String getName() {
        return "zessentials_sanction";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        SanctionType sanctionType = SanctionType.valueOf(configuration.getString(path + "sanction"));
        Duration duration = stringToDuration(configuration.getString(path + "duration", "0"));
        String reason = configuration.getString(path + "reason", "No reason");
        return new ButtonSanction(this.plugin, duration, sanctionType, reason);
    }
}
