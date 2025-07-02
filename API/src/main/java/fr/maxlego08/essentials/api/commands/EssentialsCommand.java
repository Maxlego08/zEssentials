package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Represents an essentials command.
 */
public interface EssentialsCommand {

    /**
     * Adds a sub-command to this command.
     *
     * @param alias The alias of the sub-command to add.
     */
    void addSubCommand(String alias);

    /**
     * Adds multiple aliases as sub-commands to this command.
     *
     * @param aliases The aliases of the sub-commands to add.
     */
    void addSubCommand(List<String> aliases);

    /**
     * Gets the syntax of this command.
     *
     * @return The syntax of this command.
     */
    String getSyntax();

    /**
     * Gets the sub-commands of this command.
     *
     * @return The sub-commands of this command.
     */
    List<String> getSubCommands();

    /**
     * Checks if this command ignores its parent command.
     *
     * @return true if this command ignores its parent command, false otherwise.
     */
    boolean isIgnoreParent();

    /**
     * Gets the parent command of this command.
     *
     * @return The parent command of this command.
     */
    EssentialsCommand getParent();


    EssentialsCommand getMainParent();

    /**
     * Checks if this command can be used by the console.
     *
     * @return true if the console can use this command, false otherwise.
     */
    boolean isConsoleCanUse();

    /**
     * Gets the permission required to use this command.
     *
     * @return The permission required to use this command.
     */
    String getPermission();

    /**
     * Performs pre-command execution checks and actions.
     *
     * @param plugin The essentials plugin.
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return The result of pre-command execution.
     */
    CommandResultType prePerform(EssentialsPlugin plugin, CommandSender sender, String[] args);

    /**
     * Gets the tab completer for this command.
     *
     * @return The tab completer for this command.
     */
    CommandResultType getTabCompleter();

    /**
     * Generates tab completions for this command.
     *
     * @param plugin The essentials plugin.
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return A list of tab completions.
     */
    List<String> toTab(EssentialsPlugin plugin, CommandSender sender, String[] args);

    /**
     * Checks if this command ignores its arguments.
     *
     * @return true if this command ignores its arguments, false otherwise.
     */
    boolean isIgnoreArgs();

    /**
     * Gets the description of this command.
     *
     * @return The description of this command.
     */
    String getDescription();

    /**
     * Gets the main command of this command.
     *
     * @return The main command of this command.
     */
    String getMainCommand();

    /**
     * Gets the sub-commands of this command as a list of EssentialsCommand objects.
     *
     * @return The sub-commands of this command.
     */
    List<EssentialsCommand> getSubEssentialsCommands();

    /**
     * Sets the permission required to use this command.
     *
     * @param permission The permission required to use this command.
     */
    void setPermission(String permission);
}

