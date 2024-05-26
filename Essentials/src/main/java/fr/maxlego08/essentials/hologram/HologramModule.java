package fr.maxlego08.essentials.hologram;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.TabCompletion;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.BlockHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.HologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.ItemHologramConfiguration;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.nms.r3_1_20.CraftHologram;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class HologramModule extends ZModule implements HologramManager {

    // Ajouter une commande pour activer/désactiver un hologram sans avoir besoin de le supprimer

    private final List<Hologram> holograms = new ArrayList<>();

    public HologramModule(ZEssentialsPlugin plugin) {
        super(plugin, "hologram");
        this.isRegisterEvent = false;
    }

    @Override
    public void loadConfiguration() {

        this.holograms.forEach(Hologram::deleteForAllPlayers);

        super.loadConfiguration();
        this.loadHolograms();

        HandlerList.unregisterAll(this);
        this.registerEvents();
    }

    private void registerEvents() {

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(this, this.plugin);

        List<String> events = this.holograms.stream().map(Hologram::getHologramLines).flatMap(List::stream).map(HologramLine::getEventName).filter(Objects::nonNull).distinct().toList();
        this.registerEvents(events);
    }

    @Override
    protected void updateEventPlayer(Player player, String eventName) {

        List<Hologram> holograms = this.getHologramByEvent(eventName);
        System.out.println(" > " + holograms);
        this.plugin.getScheduler().runNextTick(wrappedTask -> holograms.forEach(hologram -> hologram.updateLine(player, eventName)));
    }

    private List<Hologram> getHologramByEvent(String eventName) {
        return this.holograms.stream().filter(hologram -> hologram.getHologramLines().stream().anyMatch(hologramLine -> hologramLine.getEventName() != null && hologramLine.getEventName().equalsIgnoreCase(eventName))).toList();
    }

    @Override
    protected void updateEventUniqueId(UUID uniqueId, String eventName) {
        Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) {
            updateEventPlayer(player, eventName);
        }
    }

    @Override
    public Optional<Hologram> getHologram(String name) {
        return this.holograms.stream().filter(hologram -> hologram.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Collection<Hologram> getHolograms() {
        return Collections.unmodifiableCollection(this.holograms);
    }

    @Override
    public void addHologram(Hologram hologram) {
        this.holograms.add(hologram);
    }

    @Override
    public void removeHologram(Hologram hologram) {
        this.holograms.remove(hologram);
    }

    @Override
    public Hologram create(Player player, HologramType hologramType, String name) {

        if (getHologram(name).isPresent()) {
            message(player, Message.HOLOGRAM_CREATE_ERROR, "%name%", name);
            return null;
        }

        message(player, Message.HOLOGRAM_CREATE, "%name%", name);

        HologramConfiguration hologramConfiguration = null;
        switch (hologramType) {
            case TEXT -> hologramConfiguration = new TextHologramConfiguration();
            case BLOCK -> hologramConfiguration = new BlockHologramConfiguration();
            case ITEM -> hologramConfiguration = new ItemHologramConfiguration();
        }

        Hologram hologram = new CraftHologram(this.plugin, hologramType, hologramConfiguration, name, name, player.getLocation());

        if (hologramType == HologramType.TEXT) {
            hologram.addLine(new ZHologramLine(1, "&fUse #ff9966/holo edit " + name + " &f!"));
        }

        hologram.create();
        hologram.createForAllPlayers();
        this.saveHologram(hologram);

        addHologram(hologram);

        return hologram;
    }

    @Override
    public void delete(Player player, String name) {

        Optional<Hologram> optional = getHologram(name);
        if (optional.isEmpty()) {
            message(player, Message.HOLOGRAM_DOESNT_EXIST, "%name%", name);
            return;
        }

        Hologram hologram = optional.get();
        File file = new File(getHologramsFolder(), hologram.getName() + ".yml");
        if (file.exists()) file.delete();

        hologram.deleteForAllPlayers();
        this.holograms.remove(hologram);

        message(player, Message.HOLOGRAM_DELETE, "%name%", name);
    }

    @Override
    public void loadHolograms() {

        this.holograms.clear();

        try (Stream<Path> stream = Files.walk(Paths.get(getHologramsFolder().getPath()))) {
            stream.skip(1).map(Path::toFile).filter(File::isFile).filter(e -> e.getName().endsWith(".yml")).forEach(this::loadHologram);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveHolograms() {
        this.holograms.forEach(this::saveHologram);
    }

    @Override
    public void saveHologram(Hologram hologram) {

        Loader<Hologram> loader = new HologramLoader(this.plugin);

        File folder = getHologramsFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            File file = new File(folder, hologram.getFileName() + ".yml");

            if (!file.exists()) {
                file.createNewFile();
            }

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            loader.save(hologram, configuration, "", file);
            configuration.save(file);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void loadHologram(File file) {

        Loader<Hologram> loader = new HologramLoader(this.plugin);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        try {
            Hologram hologram = loader.load(configuration, "", file.getName().replace(".yml", ""));
            hologram.create();
            hologram.createForAllPlayers();
            this.holograms.add(hologram);
        } catch (InventoryException ignored) {
        }
    }

    private File getHologramsFolder() {
        return new File(getFolder(), "holograms");
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.holograms.forEach(hologram -> hologram.create(player));
    }

    @EventHandler
    public void onQuid(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.holograms.forEach(hologram -> hologram.removePlayer(player));
    }

    @Override
    public TabCompletion getHologramCompletion() {
        return (sender, args) -> this.holograms.stream().map(Hologram::getName).toList();
    }

    @Override
    public TabCompletion getHologramCompletion(HologramType hologramType) {
        return hologramType == null ? getHologramCompletion() : (sender, args) -> this.holograms.stream().filter(hologram -> hologram.getHologramType() == hologramType).map(Hologram::getName).toList();
    }
}
