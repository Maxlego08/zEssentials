package fr.maxlego08.essentials.loader;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.buttons.sanction.ButtonSanction;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.Duration;

public class ButtonSanctionLoader extends ButtonLoader {

    private final EssentialsPlugin plugin;

    public ButtonSanctionLoader(EssentialsPlugin plugin) {
        super(plugin, "zessentials_sanction");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        SanctionType sanctionType = SanctionType.valueOf(configuration.getString(path + "sanction"));
        Duration duration = this.plugin.getModuleManager().getModule(SanctionModule.class).stringToDuration(configuration.getString(path + "duration", "0"));
        String reason = configuration.getString(path + "reason", "No reason");
        return new ButtonSanction(this.plugin, duration, sanctionType, reason);
    }
}
