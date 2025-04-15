package fr.maxlego08.essentials.api.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple caching utility class that stores key-value pairs in memory using a ConcurrentHashMap.
 * This class is thread-safe and can be used to cache objects such as messages, to reduce the need
 * for repetitive operations like loading data from a database or a file system.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
public class SimpleCache<K, V> {

    private final ConcurrentHashMap<K, V> cache;

    /**
     * Constructs a new SimpleCache instance.
     */
    public SimpleCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the value associated with the specified key from the cache. If the key is not
     * found in the cache, the provided Loader is used to load the value, store it in the cache,
     * and then return it.
     *
     * @param key    the key whose associated value is to be returned
     * @param loader the loader used to generate the value if it is not present in the cache
     * @return the value associated with the specified key or the newly loaded value if the key
     *         was not found in the cache
     */
    public V get(K key, Loader<V> loader) {
        return cache.computeIfAbsent(key, k -> {
            V value = loader.load();
            if (value == null) {
                throw new IllegalStateException("Cache loader returned null for key: " + key);
            }
            return value;
        });
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
}
