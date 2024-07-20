package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandTeleportDeny extends VCommand {

    public CommandTeleportDeny(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TPA_DENY);
        this.setDescription(Message.DESCRIPTION_TPA_DENY);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        TeleportRequest teleportRequest = this.user.getTeleportRequest();
        if (teleportRequest == null) {
            message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
            return CommandResultType.DEFAULT;
        }

        if (!teleportRequest.isValid()) {
            this.user.setTeleportRequest(null);
            message(sender, Message.COMMAND_TPA_ERROR_TO_LATE_2);
            return CommandResultType.DEFAULT;
        }

        teleportRequest.deny();
        this.user.setTeleportRequest(null);

        return CommandResultType.SUCCESS;
    }
}
