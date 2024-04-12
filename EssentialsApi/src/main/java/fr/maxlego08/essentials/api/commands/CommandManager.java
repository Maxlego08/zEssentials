package fr.maxlego08.essentials.api.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface CommandManager extends CommandExecutor, TabCompleter {

    void registerCommand(EssentialsCommand command);

    void registerCommand(String string, EssentialsCommand command);

    void registerCommand(Plugin plugin, String string, EssentialsCommand vCommand, List<String> aliases);

    int countCommands();

    List<EssentialsCommand> getCommands();
}
