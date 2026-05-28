package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

public class PlayerListPlaceholders extends ZUtils implements PlaceholderRegister {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        String empty = plugin.getConfiguration().getPlaceholderEmpty();

        // Indexed player placeholders: %zessentials_playerlist_<index>_name%
        placeholder.register("playerlist_", (player, args) -> {

            if (args.equalsIgnoreCase("count")) {
                return String.valueOf(getOnlinePlayers(player).size());
            }
            try {
                String[] parts = args.split("_", 2);
                if (parts.length != 2) return empty;

                int index = Integer.parseInt(parts[0]);
                String property = parts[1];

                List<? extends Player> players = getOnlinePlayers(player);
                if (index < 0 || index >= players.size()) return empty;

                Player target = players.get(index);
                return getProperty(target, property, empty);
            } catch (NumberFormatException exception) {
                return empty;
            }
        }, "Returns player information at the given index (1-based)", "index", "property (name, uuid, ping, colored_ping, level, health, max_health, food_level, gamemode, world, x, y, z, displayname, is_flying, is_op, is_sneaking, is_afk)");

        // Count placeholders
        placeholder.register("playerlist_count", player -> String.valueOf(getOnlinePlayers(player).size()), "Returns the number of visible online players (excludes vanished)");
    }

    private List<? extends Player> getOnlinePlayers(Player viewer) {
        return viewer.getServer().getOnlinePlayers().stream()
                .filter(p -> !isVanishedFor(p, viewer))
                .sorted(Comparator.comparing(Player::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private String getProperty(Player target, String property, String empty) {
        return switch (property) {
            case "name" -> target.getName();
            case "uuid" -> target.getUniqueId().toString();
            case "ping" -> String.valueOf(target.getPing());
            case "colored_ping" -> {
                int ping = target.getPing();
                String color;
                if (ping < 50) color = "&a";
                else if (ping < 100) color = "&2";
                else if (ping < 200) color = "&e";
                else if (ping < 300) color = "&c";
                else color = "&4";
                yield color + ping;
            }
            case "level" -> String.valueOf(target.getLevel());
            case "health" -> DECIMAL_FORMAT.format(target.getHealth());
            case "max_health" -> {
                var attribute = target.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH);
                yield attribute != null ? DECIMAL_FORMAT.format(attribute.getValue()) : "20";
            }
            case "food_level" -> String.valueOf(target.getFoodLevel());
            case "gamemode" -> target.getGameMode().name();
            case "world" -> target.getWorld().getName();
            case "x" -> String.valueOf(target.getLocation().getBlockX());
            case "y" -> String.valueOf(target.getLocation().getBlockY());
            case "z" -> String.valueOf(target.getLocation().getBlockZ());
            case "displayname" -> target.getDisplayName();
            case "is_flying" -> String.valueOf(target.isFlying());
            case "is_op" -> String.valueOf(target.isOp());
            case "is_sneaking" -> String.valueOf(target.isSneaking());
            case "is_afk" -> {
                var metadata = target.getMetadata("afk");
                yield metadata.isEmpty() ? "false" : String.valueOf(metadata.getFirst().asBoolean());
            }
            default -> empty;
        };
    }
}
