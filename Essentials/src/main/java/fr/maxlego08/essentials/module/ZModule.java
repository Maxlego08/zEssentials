package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public abstract class ZModule implements Module {

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
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            String configKey = field.getName().replaceAll("([A-Z])", "-$1").toLowerCase();

            try {
                if (field.getType().equals(boolean.class)) {
                    field.setBoolean(this, configuration.getBoolean(configKey));
                } else if (field.getType().equals(int.class)) {
                    field.setInt(this, configuration.getInt(configKey));
                } else if (field.getType().equals(String.class)) {
                    field.set(this, configuration.getString(configKey));
                } else if (field.getType().equals(List.class)) {
                    field.set(this, configuration.getStringList(configKey));
                }
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

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
