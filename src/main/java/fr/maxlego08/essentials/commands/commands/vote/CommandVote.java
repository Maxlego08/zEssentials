package fr.maxlego08.essentials.commands.commands.vote;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.VoteModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandVote extends VCommand {

    public CommandVote(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VoteModule.class);
        this.setPermission(Permission.ESSENTIALS_VOTE_USE);
        this.setDescription(Message.DESCRIPTION_VOTE_INFORMATION);
        this.onlyPlayers();
        this.addSubCommand(new CommandVoteAdd(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getVoteManager().openVoteInventory(this.player);

        return CommandResultType.SUCCESS;
    }
}
