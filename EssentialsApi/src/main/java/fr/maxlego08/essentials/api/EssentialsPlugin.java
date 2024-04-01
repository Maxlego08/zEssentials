package fr.maxlego08.essentials.api;

import fr.maxlego08.essentials.api.commands.CommandManager;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface EssentialsPlugin extends Plugin {

    CommandManager getCommandManager();

    List<ConfigurationFile> getConfigurationFiles();

}
