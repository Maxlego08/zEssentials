package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.economy.EconomyManager;
import fr.maxlego08.essentials.module.modules.JoinQuitModule;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ZModuleManager implements ModuleManager {

    private final ZEssentialsPlugin plugin;
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();

    public ZModuleManager(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "modules");
    }

    @Override
    public void loadModules() {

        File folder = getFolder();
        if (!folder.exists()) folder.mkdirs();

        this.modules.put(TeleportationModule.class, new TeleportationModule(this.plugin));
        this.modules.put(JoinQuitModule.class, new JoinQuitModule(this.plugin));
        this.modules.put(EconomyManager.class, this.plugin.getEconomyProvider());

        this.modules.values().forEach(module -> Bukkit.getPluginManager().registerEvents(module, this.plugin));

        this.loadConfigurations();
    }

    @Override
    public void loadConfigurations() {
        this.modules.values().forEach(Module::loadConfiguration);
    }

    @Override
    public <T extends Module> T getModule(Class<T> module) {
        return (T) this.modules.get(module);
    }
}
