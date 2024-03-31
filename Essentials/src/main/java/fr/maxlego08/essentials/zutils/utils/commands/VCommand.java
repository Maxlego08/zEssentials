package fr.maxlego08.essentials.zutils.utils.commands;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.EssentialsCommand;
import fr.maxlego08.essentials.api.commands.Tab;
import fr.maxlego08.essentials.api.commands.TabCompletion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class VCommand extends Arguments implements EssentialsCommand {

    protected final EssentialsPlugin plugin;
    protected final List<VCommand> subVCommands = new ArrayList<>();
    private final List<String> subCommands = new ArrayList<>();
    private final List<String> requireArgs = new ArrayList<>();
    private final List<String> optionalArgs = new ArrayList<>();
    private final Map<Integer, TabCompletion> tabCompletions = new HashMap<>();
    private final boolean consoleCanUse = true;
    protected VCommand parent;
    protected CommandSender sender;
    protected Player player;
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
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getSyntax() {
        return syntax == null ? syntax = generateDefaultSyntax("") : syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setPlayer(Player player) {
        this.player = player;
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

    private String generateDefaultSyntax(String syntax) {
        boolean update = syntax.isEmpty();

        StringBuilder syntaxBuilder = new StringBuilder();
        if (update) {
            appendRequiredArguments(syntaxBuilder);
            appendOptionalArguments(syntaxBuilder);
            syntax = syntaxBuilder.toString().trim();
        }

        String tmpString = subCommands.get(0) + syntax;
        return parent == null ? "/" + tmpString : parent.generateDefaultSyntax(" " + tmpString);
    }

    private void appendRequiredArguments(StringBuilder syntaxBuilder) {
        requireArgs.forEach(arg -> syntaxBuilder.append(" <").append(arg).append(">"));
    }

    private void appendOptionalArguments(StringBuilder syntaxBuilder) {
        optionalArgs.forEach(arg -> syntaxBuilder.append(" [<").append(arg).append(">"));
    }

    private int parentCount(int defaultParent) {
        return parent == null ? defaultParent : parent.parentCount(defaultParent + 1);
    }

    @Override
    public boolean isConsoleCanUse() {
        return consoleCanUse;
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

    public void addCompletion(int index, TabCompletion runnable) {
        this.tabCompletions.put(index, runnable);
        this.setTabCompleter();
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
        if (this.sender instanceof Player) {
            this.player = (Player) this.sender;
        }
    }

    private CommandResultType safelyPerformCommand(EssentialsPlugin plugin) {
        try {
            return perform(plugin);
        } catch (Exception exception) {
            exception.printStackTrace();
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

        int currentInex = (args.length - this.parentCount) - 1;
        Optional<TabCompletion> optional = this.getCompletionAt(currentInex);

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
        List<String> newList = new ArrayList<>();
        for (String str : defaultList) {
            if (startWith.length() == 0 || (tab.equals(Tab.START) ? str.toLowerCase().startsWith(startWith.toLowerCase()) : str.toLowerCase().contains(startWith.toLowerCase()))) {
                newList.add(str);
            }
        }
        return newList.size() == 0 ? null : newList;
    }

}
