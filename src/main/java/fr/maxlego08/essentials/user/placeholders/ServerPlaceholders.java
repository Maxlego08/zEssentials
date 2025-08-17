package fr.maxlego08.essentials.user.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import fr.maxlego08.essentials.api.placeholders.PlaceholderRegister;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;

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
            if (players.isEmpty()) return "Empty";
            return this.lastRandomName = players.get(0).getName();
        }, "Returns a random player name online");

        placeholder.register("last_random_player", (player) -> this.lastRandomName == null ? "Empty" : this.lastRandomName, "Returns the last random player name online");

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
