package fr.maxlego08.essentials.api.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Represents a manager for handling plugin commands.
 */
public interface CommandManager extends CommandExecutor, TabCompleter, Listener {

    /**
     * Registers a command with the command manager.
     *
     * @param command The command to register.
     */
    void registerCommand(EssentialsCommand command);

    /**
     * Registers a command with the command manager using a specified label.
     *
     * @param label   The label for the command.
     * @param command The command to register.
     */
    void registerCommand(String label, EssentialsCommand command);

    /**
     * Registers a command with the command manager using a specified plugin, label, command, and aliases.
     *
     * @param plugin  The plugin registering the command.
     * @param label   The label for the command.
     * @param command The command to register.
     * @param aliases The aliases for the command.
     */
    void registerCommand(Plugin plugin, String label, EssentialsCommand command, List<String> aliases);

    /**
     * Gets the number of commands registered with the command manager.
     *
     * @return The number of commands registered.
     */
    int countCommands();

    /**
     * Gets a list of all commands registered with the command manager.
     *
     * @return A list of all registered commands.
     */
    List<EssentialsCommand> getCommands();

    /**
     * Gets a list of all commands registered with the command manager, sorted by their main command.
     *
     * @return A list of all registered commands, sorted by their main command.
     */
    List<EssentialsCommand> getSortCommands();

    /**
     * Saves all commands to the command configuration.
     */
    void saveCommands();
}

