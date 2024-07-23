package fr.maxlego08.essentials.api.storage;

/**
 * Represents a key associated with a plugin.
 */
public interface Key {

    /**
     * Gets the plugin associated with this key.
     *
     * @return the plugin name
     */
    String getPlugin();

    /**
     * Gets the name of this key.
     *
     * @return the key name
     */
    String getName();

    /**
     * Gets the full key as a string.
     *
     * @return the full key
     */
    String getKey();

}

