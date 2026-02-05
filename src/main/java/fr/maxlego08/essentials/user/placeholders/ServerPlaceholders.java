package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerPlaceholders extends ZUtils implements PlaceholderRegister {

    private final Map<String, CacheEntry> randomNumberCache = new HashMap<>();
    private String lastRandomName;
    private String lastRandomInt;

    @Override
    public void register(Placeholder placeholder, EssentialsPlugin plugin) {

        placeholder.register("server_name", (player) -> plugin.getServer().getName(), "Returns the server name");
        placeholder.register("random_player", (player) -> {
            var players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
            Collections.shuffle(players);
            if (players.isEmpty()) {
                return plugin.getConfiguration().getPlaceholderEmpty();
            }
            return this.lastRandomName = players.get(0).getName();
        }, "Returns a random player name online");

        placeholder.register("last_random_player", (player) -> this.lastRandomName == null ? plugin.getConfiguration().getPlaceholderEmpty() : this.lastRandomName, "Returns the last random player name online");

        placeholder.register("last_first_join_player", (player) -> {
            String lastFirstJoin = plugin.getLastFirstJoinPlayerName();
            return lastFirstJoin == null ? plugin.getConfiguration().getPlaceholderEmpty() : lastFirstJoin;
        }, "Returns the last player name who joined the server for the first time");

        placeholder.register("random_number_", (player, arg) -> {
            String[] args = arg.split("_");
            if (args.length != 2) return "Error: not enough arguments";
            var min = Double.parseDouble(args[0]);
            var max = Double.parseDouble(args[1]);
            var randomValue = Math.random() * (max - min) + min;
            this.lastRandomInt = String.valueOf(randomValue);
            randomNumberCache.put(player.getName(), new CacheEntry(this.lastRandomInt, System.currentTimeMillis()));
            return this.lastRandomInt;
        }, "Returns a random number between the two given arguments", "from", "to");

        placeholder.register("last_random_number_", (player, playerName) -> {
            cleanupCache();
            CacheEntry entry = randomNumberCache.get(playerName);
            if (entry == null || System.currentTimeMillis() - entry.timestamp > 3600000) {
                return "No recent random number found";
            }
            return entry.value;
        }, "Returns the last random number generated for the player within the last hour", "player name");


        placeholder.register("custom_formatted_number_", (player, arg) -> {
            String[] args = arg.split("_");
            if (args.length != 2) return "Error: not enough arguments";
            try {
                return plugin.getEconomyManager().format(PriceFormat.valueOf(args[1].toUpperCase()), Double.parseDouble(args[0]));
            } catch (Exception exception) {
                return "Format " + args[1] + " was not found";
            }
        }, "Returns a formatted number", "number", "format");

        placeholder.register("server_uptime_in_second", (player) -> String.valueOf(System.currentTimeMillis() - (plugin.getServerStartupTime() / 1000)), "Returns the server update in second");
        placeholder.register("server_uptime", (player) -> TimerBuilder.getStringTime(System.currentTimeMillis() - plugin.getServerStartupTime()), "Returns the server update in format day, hour, minutes and seconds");

        // Online players
        placeholder.register("server_online", (player) -> String.valueOf(plugin.getServer().getOnlinePlayers().size()), "Returns the number of online players");

        placeholder.register("server_max_players", (player) -> String.valueOf(plugin.getServer().getMaxPlayers()), "Returns the maximum number of players");

        placeholder.register("server_safe_online", (player) -> String.valueOf(plugin.getServer().getOnlinePlayers().stream().filter(p -> !isVanished(p)).count()), "Returns the number of non-vanished online players");

        placeholder.register("server_unique_joins", (player) -> String.valueOf(plugin.getStorageManager().getStorage().totalUsers()), "Returns the total number of unique players");

        // TPS
        DecimalFormat tpsFormat = new DecimalFormat("#.##");
        placeholder.register("server_tps", (player) -> {
            double[] tps = Bukkit.getTPS();
            return tps.length > 0 ? tpsFormat.format(Math.min(tps[0], 20.0)) : "20";
        }, "Returns the server TPS (1 minute average)");

        placeholder.register("server_tps_5", (player) -> {
            double[] tps = Bukkit.getTPS();
            return tps.length > 1 ? tpsFormat.format(Math.min(tps[1], 20.0)) : "20";
        }, "Returns the server TPS (5 minute average)");

        placeholder.register("server_tps_15", (player) -> {
            double[] tps = Bukkit.getTPS();
            return tps.length > 2 ? tpsFormat.format(Math.min(tps[2], 20.0)) : "20";
        }, "Returns the server TPS (15 minute average)");

        placeholder.register("server_tps_colored", (player) -> {
            double[] tps = Bukkit.getTPS();
            double value = tps.length > 0 ? Math.min(tps[0], 20.0) : 20.0;
            String color;
            if (value >= 18) color = "&a";
            else if (value >= 15) color = "&e";
            else color = "&c";
            return color + tpsFormat.format(value);
        }, "Returns the server TPS with color indicator");

        // Memory
        placeholder.register("server_free_memory", (player) -> String.valueOf(Runtime.getRuntime().freeMemory() / 1048576), "Returns the free memory in MB");

        placeholder.register("server_max_memory", (player) -> String.valueOf(Runtime.getRuntime().maxMemory() / 1048576), "Returns the max memory in MB");

        placeholder.register("server_used_memory", (player) -> String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576), "Returns the used memory in MB");

        placeholder.register("server_total_memory", (player) -> String.valueOf(Runtime.getRuntime().totalMemory() / 1048576), "Returns the total allocated memory in MB");

        // Per-world placeholders
        placeholder.register("server_world_players_", (player, worldName) -> {
            World world = Bukkit.getWorld(worldName);
            return world != null ? String.valueOf(world.getPlayers().size()) : "0";
        }, "Returns the number of players in a specific world", "world name");

        placeholder.register("server_world_time_", (player, worldName) -> {
            World world = Bukkit.getWorld(worldName);
            return world != null ? String.valueOf(world.getTime()) : "0";
        }, "Returns the time of a specific world in ticks", "world name");

        placeholder.register("server_world_weather_", (player, worldName) -> {
            World world = Bukkit.getWorld(worldName);
            return world != null ? (world.hasStorm() ? "Rain" : "Clear") : plugin.getConfiguration().getPlaceholderEmpty();
        }, "Returns the weather of a specific world", "world name");

    }

    private void cleanupCache() {
        Iterator<Map.Entry<String, CacheEntry>> iterator = randomNumberCache.entrySet().iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Map.Entry<String, CacheEntry> entry = iterator.next();
            if (currentTime - entry.getValue().timestamp > 3600000) {
                iterator.remove();
            }
        }
    }

    private static class CacheEntry {
        private final String value;
        private final long timestamp;

        public CacheEntry(String value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }

}
