package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandTeleportBack extends VCommand {

    public CommandTeleportBack(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_BACK);
        this.setDescription(Message.DESCRIPTION_BACK);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (this.user.getLastLocation() == null) {
            message(this.sender, Message.COMMAND_BACK_ERROR);
            return CommandResultType.DEFAULT;
        }
        
        this.user.teleport(user.getLastLocation());
        message(this.sender, Message.COMMAND_BACK);

        return CommandResultType.SUCCESS;
    }
}
