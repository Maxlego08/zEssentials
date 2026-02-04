package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerPlaceholders extends ZUtils implements PlaceholderRegister {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        var dateFormat = plugin.getConfiguration().getGlobalDateFormat();

        // Health
        placeholder.register("player_health", player -> DECIMAL_FORMAT.format(player.getHealth()), "Returns the player's current health");

        placeholder.register("player_max_health", player -> {
            AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
            return attribute != null ? DECIMAL_FORMAT.format(attribute.getValue()) : "20";
        }, "Returns the player's max health");

        placeholder.register("player_health_rounded", player -> String.valueOf(Math.round(player.getHealth())), "Returns the player's health rounded to nearest integer");

        placeholder.register("player_absorption", player -> DECIMAL_FORMAT.format(player.getAbsorptionAmount()), "Returns the player's absorption hearts");

        // Food
        placeholder.register("player_food_level", player -> String.valueOf(player.getFoodLevel()), "Returns the player's food level");

        placeholder.register("player_saturation", player -> DECIMAL_FORMAT.format(player.getSaturation()), "Returns the player's saturation level");

        placeholder.register("player_exhaustion", player -> DECIMAL_FORMAT.format(player.getExhaustion()), "Returns the player's exhaustion level");

        // Experience
        placeholder.register("player_level", player -> String.valueOf(player.getLevel()), "Returns the player's experience level");

        placeholder.register("player_exp", player -> DECIMAL_FORMAT.format(player.getExp()), "Returns the player's experience progress (0.0 to 1.0)");

        placeholder.register("player_exp_percentage", player -> String.valueOf(Math.round(player.getExp() * 100)), "Returns the player's experience progress as percentage");

        placeholder.register("player_total_exp", player -> String.valueOf(player.getTotalExperience()), "Returns the player's total experience points");

        placeholder.register("player_exp_to_level", player -> String.valueOf(getExpToLevel(player.getLevel())), "Returns the total experience required for the next level");

        // Identity
        placeholder.register("player_displayname", player -> player.getDisplayName(), "Returns the player's display name");

        placeholder.register("player_uuid", player -> player.getUniqueId().toString(), "Returns the player's UUID");

        placeholder.register("player_locale", player -> player.getLocale(), "Returns the player's client locale");

        placeholder.register("player_client_brand", player -> {
            String brand = player.getClientBrandName();
            return brand != null ? brand : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the player's client brand name");

        // Status
        placeholder.register("player_gamemode", player -> player.getGameMode().name(), "Returns the player's game mode");

        placeholder.register("player_is_flying", player -> String.valueOf(player.isFlying()), "Returns true if the player is currently flying");

        placeholder.register("player_allow_flight", player -> String.valueOf(player.getAllowFlight()), "Returns true if the player is allowed to fly");

        placeholder.register("player_is_sneaking", player -> String.valueOf(player.isSneaking()), "Returns true if the player is sneaking");

        placeholder.register("player_is_sprinting", player -> String.valueOf(player.isSprinting()), "Returns true if the player is sprinting");

        placeholder.register("player_is_sleeping", player -> String.valueOf(player.isSleeping()), "Returns true if the player is sleeping");

        placeholder.register("player_is_op", player -> String.valueOf(player.isOp()), "Returns true if the player is operator");

        placeholder.register("player_is_dead", player -> String.valueOf(player.isDead()), "Returns true if the player is dead");

        placeholder.register("player_is_swimming", player -> String.valueOf(player.isInWater()), "Returns true if the player is in water");

        placeholder.register("player_is_blocking", player -> String.valueOf(player.isBlocking()), "Returns true if the player is blocking with a shield");

        placeholder.register("player_is_gliding", player -> String.valueOf(player.isGliding()), "Returns true if the player is gliding with elytra");

        // Network
        placeholder.register("player_ping", player -> String.valueOf(player.getPing()), "Returns the player's ping in milliseconds");

        placeholder.register("player_colored_ping", player -> {
            int ping = player.getPing();
            String color;
            if (ping < 50) color = "&a";
            else if (ping < 100) color = "&2";
            else if (ping < 200) color = "&e";
            else if (ping < 300) color = "&c";
            else color = "&4";
            return color + ping;
        }, "Returns the player's ping with color based on quality");

        // Movement
        placeholder.register("player_fly_speed", player -> DECIMAL_FORMAT.format(player.getFlySpeed()), "Returns the player's fly speed");

        placeholder.register("player_walk_speed", player -> DECIMAL_FORMAT.format(player.getWalkSpeed()), "Returns the player's walk speed");

        // Air
        placeholder.register("player_remaining_air", player -> String.valueOf(player.getRemainingAir()), "Returns the player's remaining air in ticks");

        placeholder.register("player_max_air", player -> String.valueOf(player.getMaximumAir()), "Returns the player's maximum air in ticks");

        // Direction
        placeholder.register("player_compass", player -> {
            float yaw = player.getLocation().getYaw();
            yaw = ((yaw % 360) + 360) % 360;
            if (yaw >= 337.5 || yaw < 22.5) return "S";
            if (yaw < 67.5) return "SW";
            if (yaw < 112.5) return "W";
            if (yaw < 157.5) return "NW";
            if (yaw < 202.5) return "N";
            if (yaw < 247.5) return "NE";
            if (yaw < 292.5) return "E";
            return "SE";
        }, "Returns the player's compass direction (N, NE, E, SE, S, SW, W, NW)");

        placeholder.register("player_yaw", player -> DECIMAL_FORMAT.format(player.getLocation().getYaw()), "Returns the player's yaw rotation");

        placeholder.register("player_pitch", player -> DECIMAL_FORMAT.format(player.getLocation().getPitch()), "Returns the player's pitch rotation");

        // Time info
        placeholder.register("player_first_played", player -> {
            long firstPlayed = player.getFirstPlayed();
            return firstPlayed == 0 ? plugin.getConfiguration().getPlaceholderEmpty() : dateFormat.format(new Date(firstPlayed));
        }, "Returns the date when the player first joined");

        placeholder.register("player_last_played", player -> {
            long lastPlayed = player.getLastPlayed();
            return lastPlayed == 0 ? plugin.getConfiguration().getPlaceholderEmpty() : dateFormat.format(new Date(lastPlayed));
        }, "Returns the date when the player last joined");

        // Ticks lived
        placeholder.register("player_ticks_lived", player -> String.valueOf(player.getTicksLived()), "Returns the number of ticks the player has lived");

        // Inventory
        placeholder.register("player_empty_slots", player -> String.valueOf(countEmptySlots(player)), "Returns the number of empty inventory slots");

        placeholder.register("player_item_in_hand", player -> {
            var item = player.getInventory().getItemInMainHand();
            return item.getType().name();
        }, "Returns the material type of the item in main hand");

        placeholder.register("player_item_in_offhand", player -> {
            var item = player.getInventory().getItemInOffHand();
            return item.getType().name();
        }, "Returns the material type of the item in off hand");

        // World info
        placeholder.register("player_world_time", player -> String.valueOf(player.getWorld().getTime()), "Returns the time of the player's world in ticks");

        placeholder.register("player_world_time_12", player -> {
            long time = player.getWorld().getTime();
            long hours = (time / 1000 + 6) % 24;
            long minutes = (time % 1000) * 60 / 1000;
            String period = hours >= 12 ? "PM" : "AM";
            hours = hours % 12;
            if (hours == 0) hours = 12;
            return String.format("%d:%02d %s", hours, minutes, period);
        }, "Returns the world time in 12-hour format");

        placeholder.register("player_world_time_24", player -> {
            long time = player.getWorld().getTime();
            long hours = (time / 1000 + 6) % 24;
            long minutes = (time % 1000) * 60 / 1000;
            return String.format("%02d:%02d", hours, minutes);
        }, "Returns the world time in 24-hour format");

        placeholder.register("player_world_weather", player -> player.getWorld().hasStorm() ? "Rain" : "Clear", "Returns the weather of the player's world");

        // Bed location
        placeholder.register("player_has_bed", player -> String.valueOf(player.getRespawnLocation() != null), "Returns true if the player has a respawn location set");

        placeholder.register("player_bed_world", player -> {
            var bed = player.getRespawnLocation();
            return bed != null ? bed.getWorld().getName() : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the world name of the player's respawn location");

        placeholder.register("player_bed_x", player -> {
            var bed = player.getRespawnLocation();
            return bed != null ? String.valueOf(bed.getBlockX()) : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the X coordinate of the player's respawn location");

        placeholder.register("player_bed_y", player -> {
            var bed = player.getRespawnLocation();
            return bed != null ? String.valueOf(bed.getBlockY()) : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the Y coordinate of the player's respawn location");

        placeholder.register("player_bed_z", player -> {
            var bed = player.getRespawnLocation();
            return bed != null ? String.valueOf(bed.getBlockZ()) : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the Z coordinate of the player's respawn location");
    }

    private int getExpToLevel(int level) {
        if (level <= 15) return 2 * level + 7;
        if (level <= 30) return 5 * level - 38;
        return 9 * level - 158;
    }

    private int countEmptySlots(org.bukkit.entity.Player player) {
        int empty = 0;
        for (var item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType().isAir()) empty++;
        }
        return empty;
    }
}
