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
        this.addOptionalArg("yaw");
        this.addOptionalArg("pitch");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (args.length == 1) {
            Player targetPlayer = this.argAsPlayer(0);
            this.user.teleportNow(targetPlayer.getLocation());
            message(this.sender, Message.COMMAND_TP, targetPlayer);
        } else {
            String value = this.argAsString(0);
            Location location = player.getLocation();
            float yaw, pitch;

            if (isNumeric(value)) {

                double x = this.argAsDouble(0);
                double y = this.argAsDouble(1);
                double z = this.argAsDouble(2);
                yaw = (float) this.argAsDouble(3, location.getYaw());
                pitch = (float) this.argAsDouble(4, location.getPitch());

                location.set(x, y, z);
                location.setYaw(yaw);
                location.setPitch(pitch);

                this.plugin.getScheduler().teleportAsync(player, location);
                message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);
            } else {

                double x = this.argAsDouble(1);
                double y = this.argAsDouble(2);
                double z = this.argAsDouble(3);

                location.set(x, y, z);

                if (value.equalsIgnoreCase("@s")) {

                    yaw = (float) this.argAsDouble(4, player.getLocation().getYaw());
                    pitch = (float) this.argAsDouble(5, player.getLocation().getPitch());

                    location.setYaw(yaw);
                    location.setPitch(pitch);
                    this.plugin.getScheduler().teleportAsync(player, location);

                    message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);
                } else {

                    Player targetPlayer = this.argAsPlayer(0);

                    yaw = (float) this.argAsDouble(4, targetPlayer.getLocation().getYaw());
                    pitch = (float) this.argAsDouble(5, targetPlayer.getLocation().getPitch());

                    location.setYaw(yaw);
                    location.setPitch(pitch);
                    this.plugin.getScheduler().teleportAsync(targetPlayer, location);

                    message(this.sender, Message.COMMAND_TP_LOCATION_OTHER, "%x%", x, "%y%", y, "%z%", z, targetPlayer);
                }
            }
        }

        return CommandResultType.SUCCESS;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException ignored) {
            return false;
        }
        return true;
    }
}
