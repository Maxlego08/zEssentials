package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.config.models.NearDirectionReplacements;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;

public class NearPlaceholders extends ZUtils implements PlaceholderRegister {

    private static final DecimalFormat DISTANCE_FORMAT = new DecimalFormat("#.##");

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("nearest_player_name", (player) -> {
            Player nearest = getNearestPlayer(plugin, player);
            return nearest == null ? plugin.getConfiguration().getPlaceholderEmpty() : nearest.getName();
        }, "Returns the name of the nearest player");

        placeholder.register("nearest_player_distance", (player) -> {
            Player nearest = getNearestPlayer(plugin, player);
            if (nearest == null) return plugin.getConfiguration().getPlaceholderEmpty();
            double distance = player.getLocation().distance(nearest.getLocation());
            return DISTANCE_FORMAT.format(distance);
        }, "Returns the distance to the nearest player");

        placeholder.register("nearest_player_direction", (player) -> {
            Player nearest = getNearestPlayer(plugin, player);
            if (nearest == null) return plugin.getConfiguration().getPlaceholderEmpty();
            return getDirection(player, nearest, plugin);
        }, "Returns the direction arrow to the nearest player");
    }

    private Player getNearestPlayer(EssentialsPlugin plugin, Player player) {
        double distance = plugin.getConfiguration().getNearDistance(player);
        Collection<Player> players = player.getWorld().getNearbyPlayers(player.getLocation(), distance, target ->
                target != player && !isVanishedFor(target, player)
        );

        if (players.isEmpty()) return null;

        Location playerLoc = player.getLocation();
        return players.stream()
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(playerLoc)))
                .orElse(null);
    }

    private String getDirection(Player from, Player to, EssentialsPlugin plugin) {
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
