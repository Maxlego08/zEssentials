package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.economy.EconomyManager;
import fr.maxlego08.essentials.kit.KitModule;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.module.modules.JoinQuitModule;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.module.modules.SpawnModule;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.essentials.scoreboard.ZScoreboardManager;
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
        this.modules.put(SpawnModule.class, new SpawnModule(this.plugin));
        this.modules.put(WarpModule.class, new WarpModule(this.plugin));
        this.modules.put(EconomyManager.class, this.plugin.getEconomyProvider());
        this.modules.put(HomeModule.class, new HomeModule(this.plugin));
        this.modules.put(SanctionModule.class, new SanctionModule(this.plugin));
        if (plugin.isPaperVersion()) {
            this.modules.put(JoinQuitModule.class, new JoinQuitModule(this.plugin));
            this.modules.put(ChatModule.class, new ChatModule(this.plugin));
        }
        this.modules.put(MessageModule.class, new MessageModule(this.plugin));
        this.modules.put(KitModule.class, new KitModule(this.plugin));
        this.modules.put(ItemModule.class, new ItemModule(this.plugin));
        this.modules.put(ZScoreboardManager.class, this.plugin.getScoreboardManager());

        this.loadConfigurations();

        this.modules.values().stream().filter(Module::isRegisterEvent).filter(Module::isEnable).forEach(module -> Bukkit.getPluginManager().registerEvents(module, this.plugin));
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
