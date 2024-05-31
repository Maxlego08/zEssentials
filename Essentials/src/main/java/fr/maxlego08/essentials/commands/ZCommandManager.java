package fr.maxlego08.essentials.commands;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.EssentialsCommand;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ZCommandManager extends ZUtils implements CommandManager {

    private static CommandMap commandMap;
    private static Constructor<? extends PluginCommand> constructor;

    static {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
        } catch (Exception ignored) {
        }
    }

    private final ZEssentialsPlugin plugin;
    private final List<EssentialsCommand> commands = new ArrayList<>();

    public ZCommandManager(ZEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerCommand(EssentialsCommand command) {
        this.commands.add(command);
    }

    @Override
    public void registerCommand(String string, EssentialsCommand command) {
        command.addSubCommand(string);
        this.commands.add(command);
        this.plugin.getCommand(string).setExecutor(this);
        this.plugin.getCommand(string).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        for (EssentialsCommand command : this.commands) {
            if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
                if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
                    CommandResultType type = processRequirements(command, sender, args);
                    if (!type.equals(CommandResultType.CONTINUE)) return true;
                }
            } else if (args.length >= 1 && command.getParent() != null && canExecute(args, cmd.getName().toLowerCase(), command)) {
                CommandResultType type = processRequirements(command, sender, args);
                if (!type.equals(CommandResultType.CONTINUE)) return true;
            }
        }
        message(sender, Message.COMMAND_NO_ARG);
        return true;
    }

    private boolean canExecute(String[] args, String cmd, EssentialsCommand command) {
        for (int index = args.length - 1; index > -1; index--) {
            if (command.getSubCommands().contains(args[index].toLowerCase())) {
                if (command.isIgnoreArgs() && (command.getParent() == null || canExecute(args, cmd, command.getParent(), index - 1)))
                    return true;
                if (index < args.length - 1) return false;
                return canExecute(args, cmd, command.getParent(), index - 1);
            }
        }
        return false;
    }

    private boolean canExecute(String[] args, String cmd, EssentialsCommand command, int index) {
        if (index < 0 && command.getSubCommands().contains(cmd.toLowerCase())) return true;
        else if (index < 0) return false;
        else if (command.getSubCommands().contains(args[index].toLowerCase())) {
            return canExecute(args, cmd, command.getParent(), index - 1);
        } else return false;
    }

    private CommandResultType processRequirements(EssentialsCommand command, CommandSender sender, String[] strings) {

        if (!(sender instanceof Player) && !command.isConsoleCanUse()) {
            message(sender, Message.COMMAND_NO_CONSOLE);
            return CommandResultType.DEFAULT;
        }
        if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {

            CommandResultType returnType = command.prePerform(this.plugin, sender, strings);
            if (returnType == CommandResultType.SYNTAX_ERROR) {
                message(sender, Message.COMMAND_SYNTAX_ERROR, "%syntax%", command.getSyntax());
            } else if (returnType == CommandResultType.NO_PERMISSION) {
                message(sender, Message.COMMAND_NO_PERMISSION);
            }
            return returnType;
        }
        message(sender, Message.COMMAND_NO_PERMISSION);
        return CommandResultType.DEFAULT;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String str, String[] args) {

        for (EssentialsCommand command : commands) {

            if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
                return processTab(sender, command, args);
            } else {
                String[] newArgs = Arrays.copyOf(args, args.length - 1);
                if (newArgs.length >= 1 && command.getParent() != null && canExecute(newArgs, cmd.getName().toLowerCase(), command)) {
                    return processTab(sender, command, args);
                }
            }
        }

        return null;
    }

    private List<String> processTab(CommandSender sender, EssentialsCommand command, String[] args) {

        CommandResultType type = command.getTabCompleter();
        if (type.equals(CommandResultType.DEFAULT)) {

            String startWith = args[args.length - 1];

            List<String> tabCompleter = new ArrayList<>();
            for (EssentialsCommand vCommand : this.commands) {
                if ((vCommand.getParent() != null && vCommand.getParent() == command)) {
                    String cmd = vCommand.getSubCommands().get(0);
                    if (vCommand.getPermission() == null || sender.hasPermission(vCommand.getPermission())) {
                        if (startWith.length() == 0 || cmd.startsWith(startWith)) {
                            tabCompleter.add(cmd);
                        }
                    }
                }
            }
            return tabCompleter.size() == 0 ? null : tabCompleter;

        } else if (type.equals(CommandResultType.SUCCESS)) {
            return command.toTab(this.plugin, sender, args);
        }

        return null;
    }

    /**
     * Register spigot command without plugin.yml This method will allow to
     * register a command in the spigot without using the plugin.yml This saves
     * time and understanding, the plugin.yml file is clearer
     *
     * @param string   - Main command
     * @param vCommand - Command object
     * @param aliases  - Command aliases
     */
    public void registerCommand(Plugin plugin, String string, EssentialsCommand vCommand, List<String> aliases) {
        try {
            PluginCommand command = constructor.newInstance(string, plugin);
            command.setExecutor(this);
            command.setTabCompleter(this);
            command.setAliases(aliases);

            vCommand.addSubCommand(string);
            vCommand.addSubCommand(aliases);
            commands.add(vCommand);

            if (!commandMap.register(command.getName(), plugin.getDescription().getName(), command)) {
                plugin.getLogger().info("Unable to add the command " + vCommand.getSyntax());
            }

            if (vCommand.getPermission() != null) {
                Bukkit.getPluginManager().addPermission(new Permission(vCommand.getPermission(), vCommand.getDescription() == null ? "No description" : vCommand.getDescription()));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public int countCommands() {
        return this.commands.size();
    }

    @Override
    public List<EssentialsCommand> getCommands() {
        return this.commands;
    }
}
