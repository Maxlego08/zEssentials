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

    private static final double MAX_COORDINATE = 30_000_000;

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
            if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;
            this.user.teleportNow(targetPlayer.getLocation());
            message(this.sender, Message.COMMAND_TP, targetPlayer);
        } else {
            String value = this.argAsString(0);
            Location location = player.getLocation();
            float yaw, pitch;

            if (isNumeric(value) || isRelativeCoordinate(value)) {
                double x = parseCoordinate(value, location.getX());
                double y = parseCoordinate(this.argAsString(1), location.getY());
                double z = parseCoordinate(this.argAsString(2), location.getZ());

                location.set(x, y, z);
                if (!isLocationValid(location)) {
                    message(this.sender, Message.COMMAND_SYNTAX_ERROR);
                    return CommandResultType.SYNTAX_ERROR;
                }

                yaw = (float) this.argAsDouble(3, location.getYaw());
                pitch = (float) this.argAsDouble(4, location.getPitch());

                location.setYaw(yaw);
                location.setPitch(pitch);

                this.plugin.getScheduler().teleportAsync(player, location);
                message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);
            } else {

                double x = parseCoordinate(this.argAsString(1), location.getX());
                double y = parseCoordinate(this.argAsString(2), location.getY());
                double z = parseCoordinate(this.argAsString(3), location.getZ());

                location.set(x, y, z);
                if (!isLocationValid(location)) {
                    message(this.sender, Message.COMMAND_SYNTAX_ERROR);
                    return CommandResultType.SYNTAX_ERROR;
                }

                if (value.equalsIgnoreCase("@s")) {

                    yaw = (float) this.argAsDouble(4, player.getLocation().getYaw());
                    pitch = (float) this.argAsDouble(5, player.getLocation().getPitch());

                    location.setYaw(yaw);
                    location.setPitch(pitch);
                    this.plugin.getScheduler().teleportAsync(player, location);

                    message(this.sender, Message.COMMAND_TP_LOCATION, "%x%", x, "%y%", y, "%z%", z);
                } else {

                    Player targetPlayer = this.argAsPlayer(0);

                    if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;

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

    private boolean isRelativeCoordinate(String coordinate) {
        return coordinate != null && coordinate.startsWith("~");
    }

    private double parseCoordinate(String coordinate, double current) {
        if (coordinate == null) return current;
        if (coordinate.startsWith("~")) {
            if (coordinate.length() == 1) {
                return current;
            } else {
                return current + Double.parseDouble(coordinate.substring(1));
            }
        }
        return Double.parseDouble(coordinate);
    }

    private boolean isLocationValid(Location location) {
        if (location.getWorld() == null) return false;
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) return false;
        if (Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(z)) return false;
        if (Math.abs(x) > MAX_COORDINATE || Math.abs(z) > MAX_COORDINATE) return false;
        return y >= location.getWorld().getMinHeight() && y <= location.getWorld().getMaxHeight();
    }
}
