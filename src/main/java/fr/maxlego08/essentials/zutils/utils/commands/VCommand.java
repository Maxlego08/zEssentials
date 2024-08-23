package fr.maxlego08.essentials.zutils.utils.commands;

import fr.maxlego08.essentials.api.Configuration;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.EssentialsCommand;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.commands.Tab;
import fr.maxlego08.essentials.api.commands.TabCompletion;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class VCommand extends Arguments implements EssentialsCommand {

    protected final EssentialsPlugin plugin;
    protected final List<String> cooldowns = Arrays.asList(
            "1m",    // 60 seconds
            "5m",    // 300 seconds
            "10m",   // 600 seconds
            "30m",   // 1800 seconds
            "1h",    // 3600 seconds
            "2h",    // 7200 seconds
            "3h",    // 10800 seconds
            "4h",    // 14400 seconds
            "5h",    // 18000 seconds
            "6h",    // 21600 seconds
            "7h",    // 25200 seconds
            "8h",    // 28800 seconds
            "9h",    // 32400 seconds
            "10h",   // 36000 seconds
            "11h",   // 39600 seconds
            "12h",   // 43200 seconds
            "13h",   // 46800 seconds
            "14h",   // 50400 seconds
            "15h",   // 54000 seconds
            "1d",    // 86400 seconds
            "2d",    // 172800 seconds
            "3d",    // 259200 seconds
            "4d",    // 345600 seconds
            "5d",    // 432000 seconds
            "6d",    // 518400 seconds
            "7d",    // 604800 seconds
            "14d",   // 1209600 seconds
            "21d",   // 1814400 seconds
            "28d",   // 2419200 seconds
            "30d"    // 2592000 seconds
    );
    protected final List<VCommand> subVCommands = new ArrayList<>();
    private final List<String> subCommands = new ArrayList<>();
    private final List<String> requireArgs = new ArrayList<>();
    private final List<String> optionalArgs = new ArrayList<>();
    private final Map<Integer, TabCompletion> tabCompletions = new HashMap<>();
    protected VCommand parent;
    protected CommandSender sender;
    protected Player player;
    protected User user;
    protected Configuration configuration;
    protected Class<? extends Module> module;
    private boolean consoleCanUse = true;
    private boolean ignoreParent = false;
    private boolean ignoreArgs = false;
    private String permission;
    private String syntax;
    private String description;
    private int argsMinLength;
    private int argsMaxLength;
    private boolean extendedArgs = false;
    private CommandResultType tabCompleter = CommandResultType.DEFAULT;

    public VCommand(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public void setModule(Class<? extends Module> module) {
        this.module = module;
    }

    public boolean isExtendedArgs() {
        return extendedArgs;
    }

    public void setExtendedArgs(boolean extendedArgs) {
        this.extendedArgs = extendedArgs;
    }

    public EssentialsPlugin getPlugin() {
        return plugin;
    }

    public List<VCommand> getSubVCommands() {
        return subVCommands;
    }

    @Override
    public List<EssentialsCommand> getSubEssentialsCommands() {
        return subVCommands.stream().map(e -> (EssentialsCommand) e).toList();
    }

    @Override
    public List<String> getSubCommands() {
        return subCommands;
    }

    public List<String> getRequireArgs() {
        return requireArgs;
    }

    public List<String> getOptionalArgs() {
        return optionalArgs;
    }

    public Map<Integer, TabCompletion> getTabCompletions() {
        return tabCompletions;
    }

    @Override
    public VCommand getParent() {
        return parent;
    }

    public void setParent(VCommand parent) {
        this.parent = parent;
    }

    @Override
    public EssentialsCommand getMainParent() {
        if (parent == null) return null;
        if (parent.getParent() == null) return parent;
        else return parent.getMainParent();
    }

    protected User getUser(Player player) {
        return this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission.asPermission();
    }

    @Override
    public String getSyntax() {
        return syntax == null ? syntax = generateDefaultSyntax("") : syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription(Message description) {
        this.description = description.getMessageAsString();
    }

    public int getArgsMinLength() {
        return argsMinLength;
    }

    public void setArgsMinLength(int argsMinLength) {
        this.argsMinLength = argsMinLength;
    }

    public int getArgsMaxLength() {
        return argsMaxLength;
    }

    public void setArgsMaxLength(int argsMaxLength) {
        this.argsMaxLength = argsMaxLength;
    }

    public CommandSender getSender() {
        return sender;
    }

    public void setSender(CommandSender sender) {
        this.sender = sender;
    }

    protected void setTabCompleter() {
        this.tabCompleter = CommandResultType.SUCCESS;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isIgnoreParent() {
        return ignoreParent;
    }

    public void setIgnoreParent(boolean ignoreParent) {
        this.ignoreParent = ignoreParent;
    }

    @Override
    public boolean isIgnoreArgs() {
        return ignoreArgs;
    }

    public void setIgnoreArgs(boolean ignoreArgs) {
        this.ignoreArgs = ignoreArgs;
    }

    protected void onlyPlayers() {
        this.consoleCanUse = false;
    }

    @Override
    public CommandResultType getTabCompleter() {
        return tabCompleter;
    }

    public void setTabCompleter(CommandResultType tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    public Optional<TabCompletion> getCompletionAt(int index) {
        return Optional.ofNullable(this.tabCompletions.getOrDefault(index, null));
    }

    protected void addRequireArg(String message) {
        this.requireArgs.add(message);
        this.ignoreParent = this.parent == null;
        this.ignoreArgs = true;
    }

    protected void addRequirePlayerNameArg() {
        this.addRequireArg("player", getOnlinePlayers());
    }

    protected void addRequireOfflinePlayerNameArg() {
        this.addRequireArg("player", getOfflinePlayers());
    }

    protected void addOptionalOfflinePlayerNameArg() {
        this.addOptionalArg("player", getOfflinePlayers());
    }

    protected void addRequireArg(String message, TabCompletion runnable) {
        this.addRequireArg(message);
        int index = this.requireArgs.size();
        this.addCompletion(index - 1, runnable);
    }

    protected TabCompletion getOnlinePlayers() {
        return (a, b) -> this.plugin.getEssentialsServer().getPlayersNames();
    }

    protected TabCompletion getOfflinePlayers() {
        return (a, b) -> this.plugin.getEssentialsServer().getOfflinePlayersNames();
    }

    protected void addOptionalArg(String message) {
        this.optionalArgs.add(message);
        this.ignoreParent = this.parent == null;
        this.ignoreArgs = true;
    }

    protected void addBooleanOptionalArg(String message) {
        this.addOptionalArg(message);
        int index = this.requireArgs.size() + this.optionalArgs.size();
        this.addCompletion(index - 1, (a, b) -> Arrays.asList("true", "false"));
    }

    protected void addOptionalArg(String message, TabCompletion runnable) {
        this.addOptionalArg(message);
        int index = this.requireArgs.size() + this.optionalArgs.size();
        this.addCompletion(index - 1, runnable);
    }

    private String generateDefaultSyntax(String syntax) {

        boolean update = syntax.isEmpty();

        StringBuilder syntaxBuilder = new StringBuilder();
        if (update) {
            appendRequiredArguments(syntaxBuilder);
            appendOptionalArguments(syntaxBuilder);
            syntax = syntaxBuilder.toString();
        }

        String tmpString = this.subCommands.get(0) + syntax;
        return this.parent == null ? "/" + tmpString : this.parent.generateDefaultSyntax(" " + tmpString);
    }

    private void appendRequiredArguments(StringBuilder syntaxBuilder) {
        requireArgs.forEach(arg -> syntaxBuilder.append(" <").append(arg).append(">"));
    }

    private void appendOptionalArguments(StringBuilder syntaxBuilder) {
        optionalArgs.forEach(arg -> syntaxBuilder.append(" [<").append(arg).append(">]"));
    }

    private int parentCount(int defaultParent) {
        return parent == null ? defaultParent : parent.parentCount(defaultParent + 1);
    }

    @Override
    public boolean isConsoleCanUse() {
        return consoleCanUse;
    }

    protected boolean isPlayer() {
        return this.player != null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void addSubCommand(String subCommand) {
        this.subCommands.add(subCommand);
    }

    public VCommand addSubCommand(VCommand command) {
        command.setParent(this);
        this.plugin.getCommandManager().registerCommand(command);
        this.subVCommands.add(command);
        return this;
    }

    public VCommand addSubCommand(String... subCommand) {
        this.subCommands.addAll(Arrays.asList(subCommand));
        return this;
    }

    protected void addCompletion(int index, TabCompletion runnable) {
        this.tabCompletions.put(index, runnable);
        this.setTabCompleter();
    }

    @Override
    public String getMainCommand() {
        return this.subCommands.get(0);
    }

    @Override
    public void addSubCommand(List<String> aliases) {
        this.subCommands.addAll(aliases);
    }

    @Override
    public CommandResultType prePerform(EssentialsPlugin plugin, CommandSender commandSender, String[] args) {
        updateArgumentCounts();

        if (this.syntax == null) {
            this.syntax = generateDefaultSyntax("");
        }

        this.args = args;

        if (isSubCommandMatch()) {
            return CommandResultType.CONTINUE;
        }

        if (isSyntaxError(args.length)) {
            return CommandResultType.SYNTAX_ERROR;
        }

        this.sender = commandSender;
        setPlayerIfApplicable();

        return safelyPerformCommand(plugin);
    }

    private void updateArgumentCounts() {
        this.parentCount = this.parentCount(0);
        this.argsMaxLength = this.requireArgs.size() + this.optionalArgs.size() + this.parentCount;
        this.argsMinLength = this.requireArgs.size() + this.parentCount;
    }

    private boolean isSubCommandMatch() {
        String defaultString = super.argAsString(0);
        if (defaultString != null) {
            for (VCommand subCommand : subVCommands) {
                if (subCommand.getSubCommands().contains(defaultString.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSyntaxError(int argsLength) {
        return (this.argsMinLength != 0 && argsLength < this.argsMinLength) || (this.argsMaxLength != 0 && argsLength > this.argsMaxLength && !this.extendedArgs);
    }

    private void setPlayerIfApplicable() {
        if (this.sender instanceof Player player) {
            this.player = player;
            this.user = this.plugin.getStorageManager().getStorage().getUser(player.getUniqueId());
        } else {
            this.player = null;
            this.user = null;
        }
    }

    private CommandResultType safelyPerformCommand(EssentialsPlugin plugin) {
        try {

            int cooldownSeconds = 0;
            String key = this.getMainCommand();
            configuration = this.plugin.getConfiguration();

            // Check for cooldown
            if (user != null && (!this.user.hasPermission(Permission.ESSENTIALS_BYPASS_COOLDOWN) || !configuration.isEnableCooldownBypass())) {
                Optional<Integer> optional = configuration.getCooldown(this.sender, key);
                if (optional.isPresent()) {
                    cooldownSeconds = optional.get();
                    if (this.user.isCooldown(key)) {
                        long milliSeconds = this.user.getCooldown(key) - System.currentTimeMillis();
                        message(this.sender, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(milliSeconds));
                        return CommandResultType.COOLDOWN;
                    }
                }
            }

            if (this.module != null) {

                Module module = this.plugin.getModuleManager().getModule(this.module);
                if (!module.isEnable()) {
                    message(sender, Message.MODULE_DISABLE, "%name%", module.getName());
                    return CommandResultType.MODULE_DISABLE;
                }
            }

            CommandResultType commandResultType = perform(plugin);

            if (commandResultType != CommandResultType.SYNTAX_ERROR && cooldownSeconds != 0 && this.user != null && (!this.user.hasPermission(Permission.ESSENTIALS_BYPASS_COOLDOWN) || !configuration.isEnableCooldownBypass())) {
                this.user.addCooldown(key, cooldownSeconds);
            }

            return commandResultType;
        } catch (Exception exception) {

            if (plugin.getConfiguration().isEnableDebug()) {
                exception.printStackTrace();
            }
            return CommandResultType.SYNTAX_ERROR;
        }
    }

    protected abstract CommandResultType perform(EssentialsPlugin plugin);

    public boolean sameSubCommands() {
        if (this.parent == null) {
            return false;
        }
        for (String command : this.subCommands) {
            if (this.parent.getSubCommands().contains(command)) return true;
        }
        return false;
    }

    @Override
    public List<String> toTab(EssentialsPlugin plugin, CommandSender sender, String[] args) {

        this.parentCount = this.parentCount(0);

        int currentIndex = (args.length - this.parentCount) - 1;
        Optional<TabCompletion> optional = this.getCompletionAt(currentIndex);

        if (optional.isPresent()) {

            TabCompletion collectionRunnable = optional.get();
            String startWith = args[args.length - 1];
            return this.generateList(collectionRunnable.accept(sender, args), startWith);

        }

        return null;
    }

    protected List<String> generateList(List<String> defaultList, String startWith) {
        return generateList(defaultList, startWith, Tab.CONTAINS);
    }

    protected List<String> generateList(List<String> defaultList, String startWith, Tab tab) {
        List<String> generatedList = new ArrayList<>();
        for (String str : defaultList) {
            if (startWith.length() == 0 || (tab.equals(Tab.START) ? str.toLowerCase().startsWith(startWith.toLowerCase()) : str.toLowerCase().contains(startWith.toLowerCase()))) {
                generatedList.add(str);
            }
        }
        return generatedList.size() == 0 ? null : generatedList;
    }

    public void syntaxMessage() {
        List<VCommand> commands = new ArrayList<>(this.subVCommands);
        commands.sort(new VCommandComparator());
        commands.forEach(command -> {
            if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
                message(this.sender, Message.COMMAND_SYNTAXE_HELP, "%syntax%", command.getSyntax(), "%description%", command.getDescription());
            }
        });
    }

    protected void async(Runnable runnable) {
        this.plugin.getScheduler().runAsync(wrappedTask -> runnable.run());
    }

    protected void fetchUniqueId(String userName, Consumer<UUID> consumer) {
        CommandSender commandSender = this.sender;

        this.plugin.getStorageManager().getStorage().fetchUniqueId(userName, uuid -> {

            if (uuid == null) {
                message(commandSender, Message.PLAYER_NOT_FOUND, "%player%", userName);
                return;
            }

            consumer.accept(uuid);
        });
    }

    protected void isOnline(String userName, Runnable runnable) {
        CommandSender commandSender = this.sender;
        this.plugin.getScheduler().runAsync(wrappedTask -> {

            if (!this.plugin.getEssentialsServer().isOnline(userName)) {
                message(commandSender, Message.PLAYER_NOT_FOUND, "%player%", userName);
                return;
            }

            runnable.run();
        });
    }

    protected String getArgs(int start) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < this.args.length; i++) {
            if (i != start) stringBuilder.append(" ");
            stringBuilder.append(this.args[i]);
        }
        return stringBuilder.toString();
    }

    private static class VCommandComparator implements Comparator<VCommand> {
        @Override
        public int compare(VCommand command1, VCommand command2) {
            return command1.getMainCommand().compareTo(command2.getMainCommand());
        }
    }
}
