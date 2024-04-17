package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.YamlLoader;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;

public abstract class ZModule extends YamlLoader implements Module {

    protected final ZEssentialsPlugin plugin;
    protected final String name;
    protected boolean isEnable = false;
    protected boolean isRegisterEvent = true;

    public ZModule(ZEssentialsPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public void loadConfiguration() {

        File folfer = getFolder();
        if (!folfer.exists()) {
            folfer.mkdirs();
        }
        File configFile = new File(this.plugin.getDataFolder(), "modules/" + this.name + "/config.yml");
        if (!configFile.exists()) {
            this.plugin.saveResource("modules/" + name + "/config.yml", false);
        }

        YamlConfiguration configuration = getConfiguration();
        this.loadYamlConfirmation(configuration);

        this.isEnable = configuration.getBoolean("enable", true);
    }

    @Override
    public boolean isRegisterEvent() {
        return isRegisterEvent;
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

    protected void loadInventory(String inventoryName) {
        InventoryManager inventoryManager = this.plugin.getInventoryManager();

        try {
            inventoryManager.loadInventoryOrSaveResource(this.plugin, "modules/" + name + "/" + inventoryName + ".yml");
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
    }

    protected void loadPattern(String patternName) {
        PatternManager patternManager = this.plugin.getPatternManager();

        File file = new File(this.plugin.getDataFolder(), "patterns/" + patternName + ".yml");
        if (!file.exists()) {
            this.plugin.saveResource("patterns/" + patternName + ".yml", false);
        }

        try {
            patternManager.loadPattern(file);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
    }

    protected void savePattern(String patternName){
        File file = new File(this.plugin.getDataFolder(), "patterns/" + patternName + ".yml");
        if (!file.exists()) {
            this.plugin.saveResource("patterns/" + patternName + ".yml", false);
        }
    }

    protected User getUser(Entity entity) {
        return this.plugin.getStorageManager().getStorage().getUser(entity.getUniqueId());
    }
}
