package fr.maxlego08.essentials.commands;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.EssentialsCommand;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

    protected final List<EssentialsCommand> commands = new ArrayList<>();
    private final ZEssentialsPlugin plugin;

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

            if (command.getSubCommands().contains(cmd.getName().toLowerCase()) && command.getParent() == null) {
                return processTab(sender, command, args);
            } else {
                String[] newArgs = Arrays.copyOf(args, args.length - 1);
                if (newArgs.length >= 1 && command.getParent() != null && canExecute(newArgs, cmd.getName().toLowerCase(), command)) {
                    return processTab(sender, command, args);
                }
            }
        }

        return List.of();
    }

    public List<String> processTab(CommandSender sender, EssentialsCommand command, String[] args) {

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
            return tabCompleter.size() == 0 ? List.of() : tabCompleter;

        } else if (type.equals(CommandResultType.SUCCESS)) {
            var list = command.toTab(this.plugin, sender, args);
            return list == null ? List.of() : list;
        }

        return List.of();
    }

    private YamlConfiguration getCommandConfiguration() {
        File file = new File(plugin.getDataFolder(), "commands.yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    this.plugin.getLogger().severe("The commands.yml file cannot be created.");
                    return null;
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveCommandConfiguration(YamlConfiguration configuration) {
        File file = new File(plugin.getDataFolder(), "commands.yml");
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveCommands() {

        YamlConfiguration configuration = getCommandConfiguration();
        if (configuration == null) return;

        List<EssentialsCommand> essentialsCommands = getSortCommands();

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        essentialsCommands.forEach(essentialsCommand -> {

            String commandName = getCommandName(essentialsCommand);
            ConfigurationSection configurationSection = configuration.getConfigurationSection(commandName);
            if (configurationSection != null) return;

            configuration.set(commandName + ".enable", true);
            configuration.set(commandName + ".commands", essentialsCommand.getSubCommands());
            configuration.set(commandName + ".permission", essentialsCommand.getPermission());

            atomicBoolean.set(true);
        });

        if (atomicBoolean.get()) {
            this.saveCommandConfiguration(configuration);
        }
    }

    private String getCommandName(EssentialsCommand command) {
        String className = command.getClass().getSimpleName();

        StringBuilder transformedName = new StringBuilder();

        for (int i = 0; i < className.length(); i++) {
            char currentChar = className.charAt(i);
            if (Character.isUpperCase(currentChar)) {

                if (i != 0) {
                    transformedName.append('-');
                }

                transformedName.append(Character.toLowerCase(currentChar));
            } else {

                transformedName.append(currentChar);
            }
        }

        return transformedName.toString();
    }

    /**
     * Register spigot command without plugin.yml This method will allow to
     * register a command in the spigot without using the plugin.yml This saves
     * time and understanding, the plugin.yml file is clearer
     *
     * @param mainCommand       - Main command
     * @param essentialsCommand - Command object
     * @param aliases           - Command aliases
     */
    public void registerCommand(Plugin plugin, String mainCommand, EssentialsCommand essentialsCommand, List<String> aliases) {

        YamlConfiguration configuration = getCommandConfiguration();
        if (configuration != null) {
            String commandName = getCommandName(essentialsCommand);
            ConfigurationSection configurationSection = configuration.getConfigurationSection(commandName);
            if (configurationSection != null) {

                if (!configurationSection.getBoolean("enable", true)) {
                    return;
                }

                List<String> commands = configurationSection.getStringList("commands");

                if (commands.isEmpty()) {
                    this.plugin.getLogger().severe("Command " + commandName + " doesnt have commands !");
                } else {

                    mainCommand = commands.remove(0);
                    aliases = commands;
                }

                essentialsCommand.setPermission(configurationSection.getString("permission"));
            }
        }

        try {
            PluginCommand command = constructor.newInstance(mainCommand, plugin);
            command.setExecutor(this);
            command.setTabCompleter(this);
            command.setAliases(aliases);

            essentialsCommand.addSubCommand(mainCommand);
            essentialsCommand.addSubCommand(aliases);
            commands.add(essentialsCommand);

            if (!commandMap.register(command.getName(), plugin.getDescription().getName(), command)) {
                plugin.getLogger().info("Unable to add the command " + essentialsCommand.getSyntax());
            }

            if (essentialsCommand.getPermission() != null) {
                Bukkit.getPluginManager().addPermission(new Permission(essentialsCommand.getPermission(), essentialsCommand.getDescription() == null ? "No description" : essentialsCommand.getDescription()));
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

    @Override
    public List<EssentialsCommand> getSortCommands() {
        List<EssentialsCommand> essentialsCommands = new ArrayList<>();

        this.commands.stream().filter(e -> e.getParent() == null).sorted(Comparator.comparing(EssentialsCommand::getMainCommand)).forEach(command -> {
            essentialsCommands.add(command);
            essentialsCommands.addAll(this.commands.stream().filter(e -> e.getMainParent() == command).sorted(Comparator.comparing(EssentialsCommand::getMainCommand)).toList());
        });
        return essentialsCommands;
    }

    private boolean isEssentialsCommand(String command) {
        return this.commands.stream().anyMatch(e -> e.getParent() == null && e.getSubCommands().contains(command));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {

        var player = event.getPlayer();
        String[] split = event.getMessage().substring(1).split(" ");
        String key = split.length > 0 ? split[0].toLowerCase() : "";
        if (isEssentialsCommand(key)) {
            return;
        }

        User user = this.plugin.getUser(player.getUniqueId());
        int cooldownSeconds = 0;

        var configuration = this.plugin.getConfiguration();
        if (user != null && (!user.hasPermission(fr.maxlego08.essentials.api.commands.Permission.ESSENTIALS_BYPASS_COOLDOWN) || !configuration.isEnableCooldownBypass())) {
            Optional<Integer> optional = configuration.getCooldown(player, key);
            if (optional.isPresent()) {
                cooldownSeconds = optional.get();
                if (user.isCooldown(key)) {
                    event.setCancelled(true);
                    long milliSeconds = user.getCooldown(key) - System.currentTimeMillis();
                    message(player, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(milliSeconds));
                } else {
                    user.addCooldown(key, cooldownSeconds);
                }
            }
        }
    }
}
