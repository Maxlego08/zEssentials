package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandExt extends VCommand {
    public CommandExt(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXT);
        this.setDescription(Message.DESCRIPTION_EXT);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.setFireTicks(0);
        message(sender, Message.COMMAND_EXT);

        return CommandResultType.SUCCESS;
    }
}
