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

    public Hologram(EssentialsPlugin plugin, HologramType hologramType, String name, String fileName, Location location, HologramConfiguration configuration) {
        this.plugin = plugin;
        this.hologramType = hologramType;
        this.name = name;
        this.fileName = fileName;
        this.location = location;
        this.configuration = configuration;
    }

    public abstract void create(Player player);

    public abstract void delete(Player player);

    public abstract void update(Player player);

    public abstract void update();

    public abstract void create();

    public abstract void updateLocation();

    public String getFileName() {
        return fileName;
    }

    public Location getLocation() {
        return location;
    }

    public List<HologramLine> getHologramLines() {
        return hologramLines;
    }

    public String getName() {
        return name;
    }

    public void addLine(HologramLine hologramLine) {
        this.hologramLines.add(hologramLine);
    }

    public int getNextIndex() {
        return this.hologramLines.size() + 1;
    }

    public EssentialsPlugin getPlugin() {
        return plugin;
    }

    public HologramType getHologramType() {
        return hologramType;
    }

    public HologramConfiguration getConfiguration() {
        return configuration;
    }

    public void createForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::create);
    }

    public void deleteForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::delete);
    }

    public void updateForAllPlayers() {
        this.caches.clear();
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

    public Map<Player, ComponentCache> getCaches() {
        return caches;
    }

    public void removePlayer(Player player) {
        this.caches.remove(player);
    }

    public Component getComponentText(Player player) {
        AdventureComponent componentMessage = (AdventureComponent) plugin.getComponentMessage();

        ComponentCache componentCache = this.caches.computeIfAbsent(player, k -> new ComponentCache());

        if (componentCache.isEmpty()) {
            List<Component> components = this.hologramLines.stream().sorted(Comparator.comparingInt(HologramLine::getLine)).map(HologramLine::getText).map(line -> this.plugin.papi(player, line)).map(componentMessage::getComponent).toList();
            componentCache.setComponents(components);
        }

        return componentCache.merge();
    }

    public Optional<HologramLine> getHologramLine(int line) {
        return this.hologramLines.stream().filter(hologramLine -> hologramLine.getLine() == line).findFirst();
    }

    public void removeLine(int line) {
        this.hologramLines.removeIf(hologramLine -> hologramLine.getLine() == line);
        for (HologramLine hologramLine : hologramLines) {
            if (hologramLine.getLine() > line) {
                hologramLine.setLine(hologramLine.getLine() - 1);
            }
        }
    }

    public void teleport(Location location) {
        this.location = location;
        this.updateLocation();
    }

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
}
