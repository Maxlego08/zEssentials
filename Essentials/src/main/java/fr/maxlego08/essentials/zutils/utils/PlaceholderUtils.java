package fr.maxlego08.essentials.zutils.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class PlaceholderUtils extends LocationUtils {

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    protected String papi(String placeHolder, Player player) {

        if (placeHolder == null) return null;
        if (player == null) return placeHolder;
        if (!placeHolder.contains("%")) return placeHolder;

        String cacheKey = placeHolder + ";" + player.getUniqueId();
        CacheEntry cachedResult = cache.get(cacheKey);

        if (cachedResult != null && cachedResult.isValid()) {
            return cachedResult.value;
        }

        String result = PlaceholderAPI.setPlaceholders(player, placeHolder).replace("%player%", player.getName());

        cache.put(cacheKey, new CacheEntry(result, System.currentTimeMillis()));
        return result;
    }

    protected List<String> papi(List<String> placeHolders, Player player) {
        if (player == null) return placeHolders;
        return placeHolders.stream().map(placeHolder -> papi(placeHolder, player)).collect(Collectors.toList());
    }

    private static class CacheEntry {
        String value;
        long timeStamp; // Time when the cache entry was created

        public CacheEntry(String value, long timeStamp) {
            this.value = value;
            this.timeStamp = timeStamp;
        }

        public boolean isValid() {
            // Check if the cache entry is still valid (not older than 1000 milliseconds)
            return System.currentTimeMillis() - timeStamp < 1000L;
        }
    }

}
