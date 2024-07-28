package fr.maxlego08.essentials.commands.commands.vote;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.VoteModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandVotePartyRemove extends VCommand {

    public CommandVotePartyRemove(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(VoteModule.class);
        this.addSubCommand("remove");
        this.setPermission(Permission.ESSENTIALS_VOTEPARTY_REMOVE);
        this.setDescription(Message.DESCRIPTION_VOTEPARTY_REMOVE);
        this.addRequireArg("amount", (a, b) -> Arrays.asList("10", "20", "30", "40", "50", "60"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        long amount = this.argAsLong(0);
        plugin.getVoteManager().removeCurrentVotePartyAmount(amount);
        message(player, Message.COMMAND_VOTEPARTY_REMOVE, "%amount%", amount);

        return CommandResultType.SUCCESS;
    }
}
