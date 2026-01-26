package fr.maxlego08.essentials.commands.commands.weather;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.WorldInfo;

import java.util.UUID;
import java.util.stream.Collectors;

public class CommandDay extends VCommand {

    public CommandDay(EssentialsPlugin plugin) {
        super(plugin);
        super.setPermission(Permission.ESSENTIALS_DAY);
        super.setDescription(Message.DESCRIPTION_DAY);
        super.addOptionalArg("world", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList()));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        World world = this.argAsWorld(0, isPlayer() ? this.player.getWorld() : null);
        if (world == null) {
            message(sender, Message.COMMAND_TIME_ERROR);
            return CommandResultType.SYNTAX_ERROR;
        }

        UUID worldId = world.getUID();

        if (TimeTransition.isWorldTimeChanging(worldId)) {
            message(sender, Message.COMMAND_TIME_ALREADY_CHANGING, "%world%", world.getName());
            return CommandResultType.DEFAULT;
        }

        long dayTime = plugin.getConfiguration().getDayTime();
        boolean smooth = plugin.getConfiguration().isTimeSmoothChangeEnabled();

        if (smooth) {
            TimeTransition.transitionWorldTime(plugin, world, dayTime);
        } else {
            world.setFullTime(dayTime);
        }

        message(sender, Message.COMMAND_DAY, "%world%", world.getName());
        return CommandResultType.SUCCESS;
    }
}
