package fr.maxlego08.essentials.api.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The `Hologram` class represents an abstract hologram in the game,
 * managing its configuration, location, lines of text, and player-specific caches.
 * This class provides methods to create, update, delete, and manipulate holograms for players.
 */
public abstract class Hologram {

    public static final int LINE_WIDTH = 1200;
    public static final TextColor TRANSPARENT = () -> 0;

    protected final EssentialsPlugin plugin;
    protected final HologramType hologramType;
    protected final String name;
    protected final String fileName;
    protected final List<HologramLine> hologramLines = new ArrayList<>();
    protected final Map<Player, ComponentCache> caches = new HashMap<>();
    protected final HologramConfiguration configuration;
    protected Location location;

    /**
     * Constructs a new `Hologram`.
     *
     * @param plugin        the plugin instance
     * @param hologramType  the type of the hologram
     * @param name          the name of the hologram
     * @param fileName      the filename associated with the hologram
     * @param location      the location of the hologram
     * @param configuration the hologram's configuration
     */
    public Hologram(EssentialsPlugin plugin, HologramType hologramType, String name, String fileName, Location location, HologramConfiguration configuration) {
        this.plugin = plugin;
        this.hologramType = hologramType;
        this.name = name;
        this.fileName = fileName;
        this.location = location;
        this.configuration = configuration;
    }

    /**
     * Creates the hologram for a specific player.
     *
     * @param player the player for whom the hologram is created
     */
    public abstract void create(Player player);

    /**
     * Deletes the hologram for a specific player.
     *
     * @param player the player for whom the hologram is deleted
     */
    public abstract void delete(Player player);

    /**
     * Updates the hologram for a specific player.
     *
     * @param player the player for whom the hologram is updated
     */
    public abstract void update(Player player);

    /**
     * Updates the hologram for all players.
     */
    public abstract void update();

    /**
     * Creates the hologram for all players.
     */
    public abstract void create();

    /**
     * Updates the hologram's location.
     */
    public abstract void updateLocation();

    /**
     * Returns the filename associated with the hologram.
     *
     * @return the filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the location of the hologram.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the list of hologram lines.
     *
     * @return the list of hologram lines
     */
    public List<HologramLine> getHologramLines() {
        return hologramLines;
    }

    /**
     * Returns the name of the hologram.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a new line to the hologram.
     *
     * @param hologramLine the line to add
     */
    public void addLine(HologramLine hologramLine) {
        this.hologramLines.add(hologramLine);
    }

    /**
     * Returns the index for the next line to be added to the hologram.
     *
     * @return the next index
     */
    public int getNextIndex() {
        return this.hologramLines.size() + 1;
    }

    /**
     * Returns the plugin instance associated with the hologram.
     *
     * @return the plugin instance
     */
    public EssentialsPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the type of the hologram.
     *
     * @return the hologram type
     */
    public HologramType getHologramType() {
        return hologramType;
    }

    /**
     * Returns the configuration of the hologram.
     *
     * @return the configuration
     */
    public HologramConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Creates the hologram for all online players.
     */
    public void createForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::create);
    }

    /**
     * Deletes the hologram for all online players.
     */
    public void deleteForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::delete);
    }

    /**
     * Updates the hologram for all online players and clears the cache.
     */
    public void updateForAllPlayers() {
        this.caches.clear();
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    /**
     * Returns the map of player-specific component caches.
     *
     * @return the map of player-specific component caches
     */
    public Map<Player, ComponentCache> getCaches() {
        return caches;
    }

    /**
     * Removes the specified player from the cache.
     *
     * @param player the player to remove
     */
    public void removePlayer(Player player) {
        this.caches.remove(player);
    }

    /**
     * Generates and returns the combined component text for the hologram for a specific player.
     *
     * @param player the player for whom the component text is generated
     * @return the combined component text
     */
    public Component getComponentText(Player player) {
        AdventureComponent componentMessage = (AdventureComponent) plugin.getComponentMessage();

        ComponentCache componentCache = this.caches.computeIfAbsent(player, k -> new ComponentCache());

        if (componentCache.isEmpty()) {
            List<Component> components = this.hologramLines.stream()
                    .sorted(Comparator.comparingInt(HologramLine::getLine))
                    .map(HologramLine::getText)
                    .map(line -> this.plugin.papi(player, line))
                    .map(componentMessage::getComponent)
                    .toList();
            componentCache.setComponents(components);
        }

        return componentCache.merge();
    }

    /**
     * Retrieves a specific hologram line by its line number.
     *
     * @param line the line number to retrieve
     * @return an optional containing the hologram line if found
     */
    public Optional<HologramLine> getHologramLine(int line) {
        return this.hologramLines.stream()
                .filter(hologramLine -> hologramLine.getLine() == line)
                .findFirst();
    }

    /**
     * Removes a specific line from the hologram by its line number.
     *
     * @param line the line number to remove
     */
    public void removeLine(int line) {
        this.hologramLines.removeIf(hologramLine -> hologramLine.getLine() == line);
        for (HologramLine hologramLine : hologramLines) {
            if (hologramLine.getLine() > line) {
                hologramLine.setLine(hologramLine.getLine() - 1);
            }
        }
    }

    /**
     * Teleports the hologram to a new location and updates its location.
     *
     * @param location the new location to teleport the hologram to
     */
    public void teleport(Location location) {
        this.location = location;
        this.updateLocation();
    }

    /**
     * Inserts a new line before a specified line number in the hologram.
     *
     * @param line    the line number before which the new line is inserted
     * @param newLine the new line to insert
     */
    public void insertLineBefore(int line, HologramLine newLine) {
        for (HologramLine hologramLine : hologramLines) {
            if (hologramLine.getLine() >= line) {
                hologramLine.setLine(hologramLine.getLine() + 1);
            }
        }
        newLine.setLine(line);
        hologramLines.add(newLine);
        hologramLines.sort(Comparator.comparingInt(HologramLine::getLine));
    }

    /**
     * Inserts a new line after a specified line number in the hologram.
     *
     * @param line    the line number after which the new line is inserted
     * @param newLine the new line to insert
     */
    public void insertAfterLine(int line, HologramLine newLine) {
        for (HologramLine hologramLine : hologramLines) {
            if (hologramLine.getLine() > line) {
                hologramLine.setLine(hologramLine.getLine() + 1);
            }
        }
        newLine.setLine(line + 1);
        hologramLines.add(newLine);
        hologramLines.sort(Comparator.comparingInt(HologramLine::getLine));
    }

    /**
     * Updates the hologram line with a specific event name for a player.
     *
     * @param player    the player for whom the hologram line is updated
     * @param eventName the event name associated with the hologram line
     */
    public void updateLine(Player player, String eventName) {

        AdventureComponent componentMessage = (AdventureComponent) plugin.getComponentMessage();
        ComponentCache componentCache = this.caches.computeIfAbsent(player, k -> new ComponentCache());
        List<HologramLine> hologramLines = this.hologramLines.stream()
                .filter(hologramLine -> hologramLine.getEventName() != null && hologramLine.getEventName().equalsIgnoreCase(eventName))
                .toList();
        hologramLines.forEach(hologramLine -> componentCache.updateComponent(hologramLine.getLine() - 1, componentMessage.getComponent(this.plugin.papi(player, hologramLine.getText()))));

        this.update(player);
    }
}
