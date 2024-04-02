package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandMore extends VCommand {
    public CommandMore(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_MORE);
        this.setDescription(Message.DESCRIPTION_MORE);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (this.player.getInventory().getItemInMainHand().getType().isAir()) {
            message(this.sender, Message.COMMAND_MORE_ERROR);
            return CommandResultType.DEFAULT;
        }

        this.player.getInventory().getItemInMainHand().setAmount(64);
        message(this.sender, Message.COMMAND_MORE_SUCCESS);

        return CommandResultType.SUCCESS;
    }
}
