package fr.maxlego08.essentials.api;

import fr.maxlego08.essentials.api.commands.CommandManager;
import org.bukkit.plugin.Plugin;

public interface EssentialsPlugin extends Plugin {

    CommandManager getCommandManager();

}
