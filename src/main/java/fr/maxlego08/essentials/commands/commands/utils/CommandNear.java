package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.config.models.NearDirectionReplacements;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

public class CommandNear extends VCommand {

    private static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat("#.##");

    public CommandNear(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_NEAR);
        this.setDescription(Message.DESCRIPTION_NEAR);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        double distance = plugin.getConfiguration().getNearDistance(this.player);
        Collection<Player> players = this.player.getWorld().getNearbyPlayers(this.player.getLocation(), distance, player ->
                player != this.player && !isVanishedFor(this.player, player)
        );

        if (players.isEmpty()) {
            message(sender, Message.COMMAND_NEAR_EMPTY);
        } else {
            StringBuilder playersInfo = new StringBuilder();

            for (Player target : players) {
                double dist = target.getLocation().distance(player.getLocation());
                String direction = getDirection(player, target);

                String info = getMessage(
                        Message.COMMAND_NEAR_INFO,
                        "%player%", target.getName(),
                        "%distance%", DISTANCE_FORMAT.format(dist),
                        "%direction%", direction
                );
                if (playersInfo.length() > 0) playersInfo.append(", ");
                playersInfo.append(info);
            }

            message(sender, Message.COMMAND_NEAR_PLAYER, "%players%", playersInfo.toString());
        }

        return CommandResultType.SUCCESS;
    }

    private String getDirection(Player from, Player to) {
        Vector look = from.getLocation().getDirection().setY(0).normalize();
        Vector targetDir = to.getLocation().toVector().subtract(from.getLocation().toVector()).setY(0).normalize();

        double angle = Math.toDegrees(Math.atan2(targetDir.getX(), targetDir.getZ()) - Math.atan2(look.getX(), look.getZ()));
        angle = (angle + 360) % 360;

        NearDirectionReplacements conf = plugin.getConfiguration().getNearDirectionReplacements();

        if (angle <= 22.5 || angle > 337.5) return conf.forwardReplacement();
        if (angle <= 67.5) return conf.forwardLeftReplacement();
        if (angle <= 112.5) return conf.leftReplacement();
        if (angle <= 157.5) return conf.backLeftReplacement();
        if (angle <= 202.5) return conf.backReplacement();
        if (angle <= 247.5) return conf.backRightReplacement();
        if (angle <= 292.5) return conf.rightReplacement();
        return conf.forwardRightReplacement();
    }
}
