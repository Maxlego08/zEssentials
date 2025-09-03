package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandFlySpeed extends VCommand {

    public CommandFlySpeed(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_FLY_SPEED);
        this.setDescription(Message.DESCRIPTION_FLY_SPEED);
        this.addRequireArg("speed", (a, b) -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int speed = this.argAsInteger(0);

        if (speed < 0 || speed > 10) {

            message(this.sender, Message.COMMAND_SPEED_ERROR);
            return CommandResultType.DEFAULT;
        }

        player.setFlySpeed(speed / 10f);
        message(this.sender, Message.COMMAND_SPEED_FLY, "%speed%", speed, "%player%", Message.YOU.getMessageAsString());

        return CommandResultType.SUCCESS;
    }
}
