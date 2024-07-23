package fr.maxlego08.essentials.commands.commands.vote;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.VoteModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandVoteParty extends VCommand {

    public CommandVoteParty(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VoteModule.class);
        this.setPermission(Permission.ESSENTIALS_VOTEPARTY_USE);
        this.setDescription(Message.DESCRIPTION_VOTEPARTY_INFORMATION);
        this.addSubCommand(new CommandVotePartySet(plugin));
        this.addSubCommand(new CommandVotePartyAdd(plugin));
        this.addSubCommand(new CommandVotePartyRemove(plugin));
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        // ToDo

        return CommandResultType.SUCCESS;
    }
}
