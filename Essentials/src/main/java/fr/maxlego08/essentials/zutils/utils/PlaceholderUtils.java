package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.messages.BossBarMessage;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class PlaceholderUtils extends LocationUtils {

    private static final ConcurrentHashMap<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

    protected String papi(String placeHolder, Player player) {
        return PapiHelper.papi(placeHolder, player);
    }

    protected List<String> papi(List<String> placeHolders, Player player) {
        return PapiHelper.papi(placeHolders, player);
    }

    private static class CacheEntry {
        String value;
        long timeStamp; // Time when the cache entry was created

        public CacheEntry(String value, long timeStamp) {
            this.value = value;
            this.timeStamp = timeStamp;
        }

        public boolean isValid() {
            // Check if the cache entry is still valid (not older than 100 milliseconds)
            return System.currentTimeMillis() - timeStamp < 100L;
        }
    }

    public static class PapiHelper {
        public static String papi(String placeHolder, Player player) {

            if (placeHolder == null) return null;
            if (player == null) return placeHolder;
            if (!placeHolder.contains("%")) return placeHolder;

            String cacheKey = placeHolder + ";" + player.getUniqueId();
            CacheEntry cachedResult = CACHE.get(cacheKey);

            if (cachedResult != null && cachedResult.isValid()) {
                return cachedResult.value;
            }

            String result = PlaceholderAPI.setPlaceholders(player, placeHolder).replace("%player%", player.getName());

            CACHE.put(cacheKey, new CacheEntry(result, System.currentTimeMillis()));
            return result;
        }

        public static List<String> papi(List<String> placeHolders, Player player) {
            if (player == null) return placeHolders;
            return placeHolders.stream().map(placeHolder -> papi(placeHolder, player)).collect(Collectors.toList());
        }
    }

}
