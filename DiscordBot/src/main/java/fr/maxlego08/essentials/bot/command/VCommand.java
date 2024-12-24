package fr.maxlego08.essentials.bot.command;

import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.bot.DiscordBot;
import fr.maxlego08.essentials.bot.utils.Arguments;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.List;

public abstract class VCommand extends Arguments {

    private final CommandManager commandManager;
    protected Permission permission;
    protected VCommand parent;
    protected List<String> subCommands = new ArrayList<>();
    protected List<VCommand> subVCommands = new ArrayList<>();
    protected List<CommandArgument> requireArgs = new ArrayList<>();
    protected List<CommandArgument> optionalArgs = new ArrayList<>();
    protected boolean ignoreParent = false;
    protected boolean ignoreArgs = false;
    protected boolean onlyInCommandChannel = false;
    protected boolean DEBUG = true;
    protected String syntaxe;
    protected String description;
    protected int argsMinLength;
    protected int argsMaxLength;
    protected DiscordBot instance;
    protected SlashCommandInteractionEvent event;
    protected MessageChannelUnion textChannel;
    protected Guild guild;
    protected User user;
    protected Member member;
    private SlashCommandData commandData;

    public VCommand(CommandManager commandManager) {
        super();
        this.commandManager = commandManager;
    }

    public boolean isOnlyInCommandChannel() {
        return onlyInCommandChannel;
    }

    private int parentCount(int defaultParent) {
        return parent == null ? defaultParent : parent.parentCount(defaultParent + 1);
    }

    public CommandResultType prePerform(DiscordBot discordBot, String[] args, SlashCommandInteractionEvent event) {

        this.parentCount = parentCount(0);
        this.argsMaxLength = this.requireArgs.size() + this.optionalArgs.size() + this.parentCount;
        this.argsMinLength = this.requireArgs.size() + this.parentCount;

        // We generate the basic syntax if it is impossible to find it
        if (this.syntaxe == null) {
            this.syntaxe = "";
        }

        this.args = args;

        String defaultString = argAsString(0);

        if (defaultString != null) {
            for (VCommand subCommand : subVCommands) {
                if (subCommand.getSubCommands().contains(defaultString.toLowerCase()))
                    return CommandResultType.CONTINUE;
            }
        }

		/*if (this.argsMinLength != 0 && this.argsMaxLength != 0
				&& !(args.length >= this.argsMinLength && args.length <= this.argsMaxLength)) {
			return CommandType.SYNTAX_ERROR;
		}*/

        this.event = event;
        this.user = event.getUser();
        this.member = event.getMember();
        this.textChannel = event.getChannel();
        this.guild = event.getGuild();

        try {
            return perform(discordBot);
        } catch (Exception exception) {
            if (this.DEBUG) {
                System.err.println("Commands: " + this);
                System.err.println("Error:");
                exception.printStackTrace();
            }
            return CommandResultType.SYNTAX_ERROR;
        }
    }

    protected TextChannel getChannel(long id) {
        return this.guild.getTextChannelById(id);
    }

    protected abstract CommandResultType perform(DiscordBot instance);

    public Permission getPermission() {
        return permission;
    }

    public VCommand getParent() {
        return parent;
    }

    public List<String> getSubCommands() {
        return subCommands;
    }

    public List<VCommand> getSubVCommands() {
        return subVCommands;
    }

    public List<CommandArgument> getRequireArgs() {
        return requireArgs;
    }

    public List<CommandArgument> getOptionalArgs() {
        return optionalArgs;
    }

    public boolean isIgnoreParent() {
        return ignoreParent;
    }

    public boolean isIgnoreArgs() {
        return ignoreArgs;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    public String getSyntaxe() {
        return syntaxe;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description == null ? "No description" : description;
    }

    /**
     * @return the argsMinLength
     */
    public int getArgsMinLength() {
        return argsMinLength;
    }

    public int getArgsMaxLength() {
        return argsMaxLength;
    }

    public DiscordBot getInstance() {
        return instance;
    }

    protected void addRequireArg(OptionType optionType, String name, String description) {
        this.addRequireArg(optionType, name, description, new ArrayList<>());
    }

    protected void addRequireArg(OptionType optionType, String name, String description, List<CommandChoice> choices) {
        this.requireArgs.add(new CommandArgument(optionType, name, description, choices));
        this.ignoreParent = parent == null;
        this.ignoreArgs = true;
    }

    protected void addOptionalArg(CommandArgument commandArgument) {
        this.optionalArgs.add(commandArgument);
        this.ignoreParent = parent == null;
        this.ignoreArgs = true;
    }

    public VCommand addSubCommand(String subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    public VCommand addSubCommand(VCommand command) {
        command.parent = this;
        commandManager.addCommand(command);
        this.subVCommands.add(command);
        return this;
    }

    public SlashCommandData toCommandData() {
        this.commandData = Commands.slash(this.getSubCommands().getFirst(), this.getDescription());

        this.getRequireArgs().forEach(commandArgument -> {
            OptionData optionData = new OptionData(commandArgument.optionType(), commandArgument.name(), commandArgument.description());
            commandArgument.choices().forEach(choice -> optionData.addChoice(choice.name(), choice.value()));
            this.commandData.addOptions(optionData);
        });

        this.getOptionalArgs().forEach(commandArgument -> {
            this.commandData.addOption(commandArgument.optionType(), commandArgument.name(), commandArgument.description(), false);
        });
        return this.commandData;
    }

    public SlashCommandData getCommandData() {
        return commandData;
    }
}
