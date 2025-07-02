package fr.maxlego08.essentials.api.storage;

import fr.maxlego08.essentials.api.dto.ServerStorageDTO;

import java.util.List;

public interface ServerStorage {

    /**
     * Sets the contents of the server storage with the given list of server storage DTOs.
     *
     * @param serverStorageDTOS the list of server storage DTOs to be set as the contents
     */
    void setContents(List<ServerStorageDTO> serverStorageDTOS);

    /**
     * Retrieves the long value associated with the specified key in the server storage.
     * If the key does not exist in the server storage, a default value of 0 is returned.
     *
     * @param key the key for which the long value is to be retrieved
     * @return the long value associated with the specified key, or 0 if the key does not exist
     */
    long getLong(Key key);

    /**
     * Gets the long value associated with the specified key in the server storage.
     * If the key does not exist in the server storage, the default value is returned.
     *
     * @param key          the key to retrieve the value for
     * @param defaultValue the default value to return if the key does not exist
     * @return the long value associated with the specified key, or the default value if the key does not exist
     */
    long getLong(Key key, long defaultValue);

    /**
     * Gets the integer value associated with the specified key in the server storage.
     * If the key does not exist in the server storage, 0 is returned.
     *
     * @param key the key to retrieve the value for
     * @return the integer value associated with the specified key, or 0 if the key does not exist
     */
    int getInt(Key key);

    /**
     * Gets the integer value associated with the specified key in the server storage.
     * If the key does not exist in the server storage, the default value is returned.
     *
     * @param key          the key to retrieve the value for
     * @param defaultValue the default value to return if the key does not exist
     * @return the integer value associated with the specified key, or the default value if the key does not exist
     */
    int getInt(Key key, int defaultValue);

    /**
     * Gets the string value associated with the specified key in the server storage.
     *
     * @param key the key for which the string value is to be retrieved
     * @return the string value associated with the specified key
     */
    String getString(Key key);

    /**
     * Gets the string value associated with the specified key in the server storage.
     *
     * @param key          the key for which the string value is to be retrieved
     * @param defaultValue the default value to return if the key does not exist
     * @return the string value associated with the specified key, or the default value if the key does not exist
     */
    String getString(Key key, String defaultValue);

    /**
     * Gets the boolean value associated with the specified key in the server storage.
     *
     * @param key the key for which the boolean value is to be retrieved
     * @return the boolean value associated with the specified key, or false if the key does not exist
     */
    boolean getBoolean(Key key);

    /**
     * Gets the boolean value associated with the specified key in the server storage.
     * If the key does not exist in the server storage, the default value is returned.
     *
     * @param key          the key to retrieve the value for
     * @param defaultValue the default value to return if the key does not exist
     * @return the value associated with the key, or the default value if the key does not exist
     */
    boolean getBoolean(Key key, boolean defaultValue);

    /**
     * Checks if the specified key exists in the server storage.
     *
     * @param key the key to be checked for existence
     * @return true if the key exists in the server storage, false otherwise
     */
    boolean exist(Key key);

    /**
     * Sets the value associated with the specified key in the server storage.
     *
     * @param key    the key for which the value is to be set
     * @param object the value to be associated with the specified key
     */
    void set(Key key, Object object);
}
