package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandAfk extends VCommand {

    public CommandAfk(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_AFK);
        this.setDescription(Message.DESCRIPTION_AFK);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        boolean enable = !this.user.isManualAfk();
        this.user.setAfk(enable);
        this.user.setLastActiveTime();

        if (enable) {
            message(this.player, Message.COMMAND_AFK_ENABLE);
        } else {
            message(this.player, Message.COMMAND_AFK_DISABLE);
        }

        return CommandResultType.SUCCESS;
    }
}

