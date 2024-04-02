package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.zutils.utils.YamlLoader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class ZModule extends YamlLoader implements Module {

    protected final ZEssentialsPlugin plugin;
    protected final String name;
    protected boolean isEnable = false;

    public ZModule(ZEssentialsPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public void loadConfiguration() {

        File folfer = getFolder();
        if (!folfer.exists()) {
            folfer.mkdirs();
            this.plugin.saveResource("modules/" + name + "/config.yml", false);
        }

        YamlConfiguration configuration = getConfiguration();
        this.loadYamlConfirmation(configuration);

        this.isEnable = configuration.getBoolean("enable", true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "modules/" + name);
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(getFolder(), "config.yml"));
    }

    @Override
    public boolean isEnable() {
        return this.isEnable;
    }
}
