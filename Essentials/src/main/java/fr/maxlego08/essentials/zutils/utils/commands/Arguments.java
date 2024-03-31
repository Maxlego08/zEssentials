package fr.maxlego08.essentials.zutils.utils.commands;

import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * This abstract class is designed to handle command arguments, allowing for easy retrieval
 * and conversion of argument values into various data types. It supports operations such as
 * parsing strings, booleans, integers, longs, doubles, and specific Bukkit types like
 * {@link Player}, {@link OfflinePlayer}, {@link EntityType}, and {@link World}.
 */
public abstract class Arguments extends ZUtils {

    protected String[] args;
    protected int parentCount = 0;

    /**
     * Retrieves the argument at the specified index as a String.
     *
     * @param index The index of the argument to retrieve, adjusted by parent command count.
     * @return The argument at the specified index as a String, or null if an exception occurs.
     */
    protected String argAsString(int index) {
        try {
            return this.args[index + this.parentCount];
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Retrieves the argument at the specified index as a String, with a default value.
     *
     * @param index        The index of the argument to retrieve, adjusted by parent command count.
     * @param defaultValue The default value to return if the argument cannot be retrieved.
     * @return The argument at the specified index as a String, or the default value if an exception occurs.
     */
    protected String argAsString(int index, String defaultValue) {
        try {
            return this.args[index + this.parentCount];
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts and returns the argument at the specified index as a boolean.
     *
     * @param index The index of the argument to convert, adjusted by parent command count.
     * @return The argument converted to a boolean.
     */
    protected boolean argAsBoolean(int index) {
        return Boolean.valueOf(argAsString(index));
    }

    /**
     * Converts and returns the argument at the specified index as a boolean, with a default value.
     *
     * @param index        The index of the argument to convert, adjusted by parent command count.
     * @param defaultValue The default value to return if the argument cannot be converted.
     * @return The argument converted to a boolean, or the default value if an exception occurs.
     */
    protected boolean argAsBoolean(int index, boolean defaultValue) {
        try {
            return Boolean.valueOf(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts and returns the argument at the specified index as an integer.
     *
     * @param index The index of the argument to convert, adjusted by parent command count.
     * @return The argument converted to an integer.
     */
    protected int argAsInteger(int index) {
        return Integer.valueOf(argAsString(index));
    }

    /**
     * Converts and returns the argument at the specified index as an integer, with a default value.
     *
     * @param index        The index of the argument to convert, adjusted by parent command count.
     * @param defaultValue The default value to return if the argument cannot be converted.
     * @return The argument converted to an integer, or the default value if an exception occurs.
     */
    protected int argAsInteger(int index, int defaultValue) {
        try {
            return Integer.valueOf(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts and returns the argument at the specified index as a long.
     *
     * @param index The index of the argument to convert, adjusted by parent command count.
     * @return The argument converted to a long.
     */
    protected long argAsLong(int index) {
        return Long.valueOf(argAsString(index));
    }

    /**
     * Converts and returns the argument at the specified index as a long, with a default value.
     *
     * @param index        The index of the argument to convert, adjusted by parent command count.
     * @param defaultValue The default value to return if the argument cannot be converted.
     * @return The argument converted to a long, or the default value if an exception occurs.
     */
    protected long argAsLong(int index, long defaultValue) {
        try {
            return Long.valueOf(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts and returns the argument at the specified index as a double, with a default value.
     *
     * @param index        The index of the argument to convert, adjusted by parent command count.
     * @param defaultValue The default value to return if the argument cannot be converted.
     * @return The argument converted to a double, or the default value if an exception occurs.
     */
    protected double argAsDouble(int index, double defaultValue) {
        try {
            return Double.valueOf(argAsString(index).replace(",", "."));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts and returns the argument at the specified index as a double.
     *
     * @param index The index of the argument to convert, adjusted by parent command count.
     * @return The argument converted to a double.
     */
    protected double argAsDouble(int index) {
        return Double.valueOf(argAsString(index).replace(",", "."));
    }

    /**
     * Attempts to retrieve a {@link Player} based on the argument at the specified index.
     *
     * @param index The index of the argument to use for the player lookup, adjusted by parent command count.
     * @return The player if found, otherwise null.
     */
    protected Player argAsPlayer(int index) {
        return Bukkit.getPlayer(argAsString(index));
    }

    /**
     * Attempts to retrieve a {@link Player} based on the argument at the specified index, with a default value.
     *
     * @param index        The index of the argument to use for the player lookup, adjusted by parent command count.
     * @param defaultValue The default value to return if the player cannot be found.
     * @return The player if found, otherwise the default value.
     */
    protected Player argAsPlayer(int index, Player defaultValue) {
        try {
            return Bukkit.getPlayer(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Attempts to retrieve an {@link OfflinePlayer} based on the argument at the specified index.
     *
     * @param index The index of the argument to use for the offline player lookup, adjusted by parent command count.
     * @return The offline player if found, otherwise null.
     */
    protected OfflinePlayer argAsOfflinePlayer(int index) {
        return Bukkit.getOfflinePlayer(argAsString(index));
    }

    /**
     * Attempts to retrieve an {@link OfflinePlayer} based on the argument at the specified index, with a default value.
     *
     * @param index        The index of the argument to use for the offline player lookup, adjusted by parent command count.
     * @param defaultValue The default value to return if the offline player cannot be found.
     * @return The offline player if found, otherwise the default value.
     */
    protected OfflinePlayer argAsOfflinePlayer(int index, OfflinePlayer defaultValue) {
        try {
            return Bukkit.getOfflinePlayer(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts the argument at the specified index to an {@link EntityType}.
     *
     * @param index The index of the argument to convert, adjusted by parent command count.
     * @return The {@link EntityType} corresponding to the argument.
     */
    protected EntityType argAsEntityType(int index) {
        return EntityType.valueOf(argAsString(index).toUpperCase());
    }

    /**
     * Converts the argument at the specified index to an {@link EntityType}, with a default value.
     *
     * @param index        The index of the argument to convert, adjusted by parent command count.
     * @param defaultValue The default value to return if the entity type cannot be determined.
     * @return The {@link EntityType} corresponding to the argument, or the default value if an exception occurs.
     */
    protected EntityType argAsEntityType(int index, EntityType defaultValue) {
        try {
            return EntityType.valueOf(argAsString(index).toUpperCase());
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Attempts to retrieve a {@link World} based on the argument at the specified index.
     *
     * @param index The index of the argument to use for the world lookup, adjusted by parent command count.
     * @return The world if found, otherwise null.
     */
    protected World argAsWorld(int index) {
        try {
            return Bukkit.getWorld(argAsString(index));
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Attempts to retrieve a {@link World} based on the argument at the specified index, with a specified default world.
     *
     * @param index        The index of the argument to use for the world lookup, adjusted by parent command count.
     * @param defaultValue The default world to return if the specified world cannot be found.
     * @return The world if found, otherwise the specified default world.
     */
    protected World argAsWorld(int index, World defaultValue) {
        try {
            return Bukkit.getWorld(argAsString(index));
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

}
