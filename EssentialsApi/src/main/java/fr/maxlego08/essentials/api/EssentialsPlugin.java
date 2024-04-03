package fr.maxlego08.essentials.api;

import com.google.gson.Gson;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.database.MigrationManager;
import fr.maxlego08.essentials.api.modules.ModuleManager;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.storage.Persist;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface EssentialsPlugin extends Plugin {

    CommandManager getCommandManager();

    StorageManager getStorageManager();

    List<ConfigurationFile> getConfigurationFiles();

    Gson getGson();

    Persist getPersist();

    ServerImplementation getScheduler();

    ModuleManager getModuleManager();

    InventoryManager getInventoryManager();

    ButtonManager getButtonManager();

    PatternManager getPatternManager();

    Placeholder getPlaceholder();

    Configuration getConfiguration();

    MigrationManager getMigrationManager();
}
