package fr.maxlego08.essentials.api.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * An enhanced caching utility class that stores key-value pairs with an expiration time in memory using a ConcurrentHashMap.
 * This class is thread-safe and can be used to cache objects, automatically refreshing them if they expire.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class ExpiringCache<K, V> {

    private final ConcurrentHashMap<K, CacheEntry<V>> cache;
    private final long expiryDurationMillis;

    /**
     * Constructs a new ExpiringCache instance with a specified expiry duration.
     *
     * @param expiryDurationMillis the duration in milliseconds after which a cache entry should expire
     */
    public ExpiringCache(long expiryDurationMillis) {
        this.cache = new ConcurrentHashMap<>();
        this.expiryDurationMillis = expiryDurationMillis;
    }

    /**
     * Retrieves the value associated with the specified key from the cache. If the key is not found,
     * or the entry has expired, the provided Loader is used to load the value, store it in the cache,
     * and then return it.
     * <p>
     * Supports soft expiration: if the entry is soft-expired but not hard-expired, it returns the old value
     * and refreshes the cache asynchronously.
     *
     * @param key    the key whose associated value is to be returned
     * @param loader the loader used to generate the value if it is not present or expired in the cache
     * @return the value associated with the specified key or the newly loaded value if the key
     * was not found in the cache or if the entry expired
     */
    public V get(K key, Loader<V> loader) {
        CacheEntry<V> entry = cache.get(key);
        long currentTime = System.currentTimeMillis();

        // 1. Hard Expiration or Not Present -> Synchronous Load
        if (entry == null || entry.expiryTime < currentTime) {
            return cache.compute(key, (k, v) -> {
                // Double-check inside lock
                long now = System.currentTimeMillis();
                if (v == null || v.expiryTime < now) {
                    V newValue = loader.load();
                    long expiry = now + expiryDurationMillis;
                    long softExpiry = now + (long) (expiryDurationMillis * 0.8); // Refresh at 80% of ttl
                    return new CacheEntry<>(newValue, expiry, softExpiry);
                }
                return v;
            }).value;
        }

        // 2. Soft Expiration -> Return Old Value & Asynchronous Refresh
        if (entry.softExpiryTime < currentTime && entry.isUpdating.compareAndSet(false, true)) {
            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    V newValue = loader.load();
                    long now = System.currentTimeMillis();
                    long expiry = now + expiryDurationMillis;
                    long softExpiry = now + (long) (expiryDurationMillis * 0.8);
                    cache.put(key, new CacheEntry<>(newValue, expiry, softExpiry));
                } catch (Exception e) {
                    e.printStackTrace();
                    // Reset flag on failure so we can try again later
                    entry.isUpdating.set(false);
                }
            });
        }

        return entry.value;
    }

    /**
     * Clears the entry associated with the specified key from the cache.
     *
     * @param key the key whose associated value is to be cleared from the cache
     */
    public void clear(K key) {
        cache.remove(key);
    }


    /**
     * Functional interface for loading values into the cache. Implementations of this interface
     * provide a method to load a value, typically involving an operation such as fetching data
     * from a database or a remote service.
     *
     * @param <V> the type of value to be loaded
     */
    @FunctionalInterface
    public interface Loader<V> {
        /**
         * Loads a value.
         *
         * @return the loaded value
         */
        V load();
    }

    /**
     * A simple cache entry that holds the value and its expiry time.
     */
    private static class CacheEntry<V> {
        final V value;
        final long expiryTime;
        final long softExpiryTime;
        final java.util.concurrent.atomic.AtomicBoolean isUpdating = new java.util.concurrent.atomic.AtomicBoolean(false);

        CacheEntry(V value, long expiryTime, long softExpiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
            this.softExpiryTime = softExpiryTime;
        }
    }
}
