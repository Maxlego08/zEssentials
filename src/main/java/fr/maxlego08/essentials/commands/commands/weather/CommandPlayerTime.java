package fr.maxlego08.essentials.commands.commands.weather;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;

public class CommandPlayerTime extends VCommand {

    public CommandPlayerTime(EssentialsPlugin plugin) {
        super(plugin);
        super.setPermission(Permission.ESSENTIALS_PLAYER_TIME);
        super.setDescription(Message.DESCRIPTION_PLAYER_TIME);
        super.addOptionalArg("time", (a, b) -> Arrays.asList("0", "500", "1000", "1500", "2000"));
        super.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        long ticks = argAsLong(0, 0);

        if (ticks == 0) {

            player.resetPlayerTime();
            message(player, Message.COMMAND_PLAYER_TIME_RESET);

        } else {

            long time = player.getPlayerTime();
            time -= time % 24000L;
            time += 24000L + ticks;

            player.setPlayerTime(time, false);
            message(player, Message.COMMAND_PLAYER_TIME_CHANGE);
        }

        return CommandResultType.SUCCESS;
    }
}
