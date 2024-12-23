package fr.maxlego08.essentials.bot.command.commands;

import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.bot.DiscordBot;
import fr.maxlego08.essentials.bot.command.CommandManager;
import fr.maxlego08.essentials.bot.command.VCommand;
import net.dv8tion.jda.api.Permission;

public class CommandSetLinkMessage extends VCommand {

    public CommandSetLinkMessage(CommandManager commandManager) {
        super(commandManager);
        this.permission = Permission.ADMINISTRATOR;
    }

    @Override
    protected CommandResultType perform(DiscordBot instance) {

        instance.getLinkManager().sendLinkMessage(event, this.textChannel);

        return CommandResultType.SUCCESS;
    }

}