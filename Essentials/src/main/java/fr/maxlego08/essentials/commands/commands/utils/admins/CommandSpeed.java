package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSpeed extends VCommand {
    public CommandSpeed(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_SPEED);
        this.setDescription(Message.DESCRIPTION_SPEED);
        this.addRequireArg("speed", (a, b) -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int speed = this.argAsInteger(0);
        Player player = this.argAsPlayer(1, this.player);

        if (speed < 0 || speed > 10) {

            message(this.sender, Message.COMMAND_SPEED_ERROR);
            return CommandResultType.DEFAULT;
        }

        if (player == null) {
            message(sender, Message.COMMAND_SPEED_INVALID);
            return CommandResultType.DEFAULT;
        }

        float speedFloat = speed / 10f;

        boolean isFlying = player.isFlying();
        Message speedTypeMessage = isFlying ? Message.COMMAND_SPEED_FLY : Message.COMMAND_SPEED_WALK;

        if (isFlying) {
            player.setFlySpeed(speedFloat);
        } else {
            player.setWalkSpeed(speedFloat);
        }

        String playerNameOrYou = player == this.player ? Message.YOU.getMessage() : player.getName();
        message(this.sender, speedTypeMessage, "%speed%", speed, "%player%", playerNameOrYou);


        return CommandResultType.SUCCESS;
    }
}
