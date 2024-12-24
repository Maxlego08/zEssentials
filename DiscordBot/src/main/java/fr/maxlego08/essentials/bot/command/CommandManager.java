package fr.maxlego08.essentials.bot.command;

import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.bot.DiscordBot;
import fr.maxlego08.essentials.bot.command.commands.CommandReload;
import fr.maxlego08.essentials.bot.command.commands.CommandSetLinkMessage;
import fr.maxlego08.essentials.bot.command.commands.CommandStop;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final DiscordBot instance;
    private final List<VCommand> commands = new ArrayList<VCommand>();

    public CommandManager(DiscordBot instance) {
        super();
        this.instance = instance;
        registerCommands();
    }

    public void registerCommands() {
        this.commands.clear();
        registerCommand("stop", new CommandStop(this), "end");
        registerCommand("setlinkmessage", new CommandSetLinkMessage(this));
        registerCommand("reload", new CommandReload(this));
    }

    public void addCommand(VCommand command) {
        this.commands.add(command);
    }

    public void registerCommand(String cmd, VCommand command, String... strings) {
        command.subCommands.add(cmd);
        command.subCommands.addAll(Arrays.asList(strings));
        this.commands.add(command);
    }

    public void onCommand(String cmd, String[] args, SlashCommandInteractionEvent event) {
        for (VCommand command : this.commands) {
            if (command.getSubCommands().contains(cmd.toLowerCase())) {
                if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
                    CommandResultType type = processRequirements(command, args, event);
                    if (!type.equals(CommandResultType.CONTINUE)) {
                        return;
                    }
                }
            } else if (args.length >= 1 && command.getParent() != null && canExecute(args, cmd.toLowerCase(), command)) {
                CommandResultType type = processRequirements(command, args, event);
                if (!type.equals(CommandResultType.CONTINUE)) {
                    return;
                }
            }
        }
    }

    private boolean canExecute(String[] args, String cmd, VCommand command) {
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

    private boolean canExecute(String[] args, String cmd, VCommand command, int index) {
        if (index < 0 && command.getSubCommands().contains(cmd.toLowerCase())) return true;
        else if (index < 0) return false;
        else if (command.getSubCommands().contains(args[index].toLowerCase()))
            return canExecute(args, cmd, command.getParent(), index - 1);
        else return false;
    }

    private CommandResultType processRequirements(VCommand command, String[] strings, SlashCommandInteractionEvent event) {

        if (event == null) return CommandResultType.DEFAULT;
        var member = event.getMember();
        if (member == null) return CommandResultType.DEFAULT;

        if (command.getPermission() == null || member.hasPermission(command.getPermission())) {
            return command.prePerform(instance, strings, event);
        }

        event.reply("You don't have permission to use this command").setEphemeral(true).queue();
        return CommandResultType.DEFAULT;
    }

    public List<VCommand> getCommands() {
        return commands;
    }
}
