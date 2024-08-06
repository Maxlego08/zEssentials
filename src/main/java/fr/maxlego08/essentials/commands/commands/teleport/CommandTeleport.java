package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandTeleport extends VCommand {

    public CommandTeleport(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TP);
        this.setDescription(Message.DESCRIPTION_TP);
        this.addRequirePlayerNameArg();
        this.addOptionalArg("x");
        this.addOptionalArg("y");
        this.addOptionalArg("z");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (args.length == 1) {

            Player targetPlayer = this.argAsPlayer(0);
            this.user.teleportNow(targetPlayer.getLocation());
            message(this.sender, Message.COMMAND_TP, "%player%", targetPlayer);

        } else if (args.length == 3) {

            double x = this.argAsDouble(0);
            double y = this.argAsDouble(1);
            double z = this.argAsDouble(2);

            Location location = player.getLocation();
            location.set(x, y, z);
            player.teleport(location);
            message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);

        } else if (args.length == 4) {

            String value = this.argAsString(0);
            double x = this.argAsDouble(1);
            double y = this.argAsDouble(2);
            double z = this.argAsDouble(3);

            Location location = player.getLocation();
            location.set(x, y, z);

            if (value.equalsIgnoreCase("@s")) {

                player.teleport(location);
                message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);

            } else {

                Player targetPlayer = this.argAsPlayer(0);
                targetPlayer.teleport(location);
                message(this.sender, Message.COMMAND_TP_LOCATION_OTHER, "%x%", x, "%y%", y, "%z%", z, "%player%", targetPlayer.getName());

            }
        } else return CommandResultType.SYNTAX_ERROR;


        return CommandResultType.SUCCESS;
    }
}
