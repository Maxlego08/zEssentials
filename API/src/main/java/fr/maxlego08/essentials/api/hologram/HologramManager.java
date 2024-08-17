package fr.maxlego08.essentials.api.hologram;

import fr.maxlego08.essentials.api.commands.TabCompletion;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface for managing holograms in the plugin.
 * Provides methods for creating, retrieving, saving, and deleting holograms.
 */
public interface HologramManager extends Module {

    /**
     * Retrieves a hologram by its name.
     *
     * @param name the name of the hologram
     * @return an {@link Optional} containing the hologram if found, otherwise empty
     */
    Optional<Hologram> getHologram(String name);

    /**
     * Retrieves all holograms currently managed by this manager.
     *
     * @return a collection of all holograms
     */
    Collection<Hologram> getHolograms();

    /**
     * Adds a new hologram to the manager.
     *
     * @param hologram the hologram to add
     */
    void addHologram(Hologram hologram);

    /**
     * Removes a hologram from the manager.
     *
     * @param hologram the hologram to remove
     */
    void removeHologram(Hologram hologram);

    /**
     * Creates a new hologram for a player.
     *
     * @param player       the player creating the hologram
     * @param hologramType the type of hologram to create
     * @param name         the name of the hologram
     * @return the created hologram
     */
    Hologram create(Player player, HologramType hologramType, String name);

    /**
     * Deletes a hologram by name for a player.
     *
     * @param player the player requesting the deletion
     * @param name   the name of the hologram to delete
     */
    void delete(Player player, String name);

    /**
     * Loads all holograms from the storage.
     */
    void loadHolograms();

    /**
     * Saves all holograms to the storage.
     */
    void saveHolograms();

    /**
     * Saves a specific hologram to the storage.
     *
     * @param hologram the hologram to save
     */
    void saveHologram(Hologram hologram);

    /**
     * Loads a hologram from a specific file.
     *
     * @param file the file to load the hologram from
     */
    void loadHologram(File file);

    /**
     * Retrieves a tab completion object for hologram names.
     *
     * @return a {@link TabCompletion} object for hologram names
     */
    TabCompletion getHologramCompletion();

    /**
     * Retrieves a tab completion object for hologram names filtered by type.
     *
     * @param hologramType the type of hologram to filter by
     * @return a {@link TabCompletion} object for hologram names of the specified type
     */
    TabCompletion getHologramCompletion(HologramType hologramType);

    /**
     * Creates a new hologram with the specified configuration and location.
     *
     * @param hologramType  the type of hologram to create
     * @param configuration the configuration for the hologram
     * @param fileName      the name of the file to associate with the hologram
     * @param name          the name of the hologram
     * @param location      the location to place the hologram
     * @return the created hologram
     */
    Hologram createHologram(HologramType hologramType, HologramConfiguration configuration, String fileName, String name, Location location);
}
