package fr.maxlego08.essentials.api.commands;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface EssentialsCommand {

    void addSubCommand(String string);

    void addSubCommand(List<String> aliases);

    String getSyntax();

    List<String> getSubCommands();

    boolean isIgnoreParent();

    EssentialsCommand getParent();

    boolean isConsoleCanUse();

    String getPermission();

    CommandResultType prePerform(EssentialsPlugin plugin, CommandSender sender, String[] strings);

    CommandResultType getTabCompleter();

    List<String> toTab(EssentialsPlugin plugin, CommandSender sender, String[] args);

    boolean isIgnoreArgs();
}
