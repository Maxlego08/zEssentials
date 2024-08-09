package fr.maxlego08.essentials.hologram;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.TabCompletion;
import fr.maxlego08.essentials.api.hologram.DamageIndicatorConfiguration;
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
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.menu.zcore.utils.nms.NmsVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class HologramModule extends ZModule implements HologramManager {

    // Ajouter une commande pour activer/désactiver un hologram sans avoir besoin de le supprimer

    private final List<Hologram> holograms = new ArrayList<>();
    private final Map<String, List<File>> pendingHolograms = new HashMap<>();
    private DamageIndicatorConfiguration damageIndicator;

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

        Hologram hologram = this.createHologram(hologramType, hologramConfiguration, name, name, player.getLocation());

        if (hologramType == HologramType.TEXT) {
            hologram.addLine(new ZHologramLine(1, "&fUse #ff9966/holo editline " + name + " 1 <your text>&f!"));
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

        File folder = getHologramsFolder();
        if (!folder.exists()) {
            folder.mkdirs();
            this.plugin.saveResource("modules/hologram/holograms/baltop.yml.example", false);
        }

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

        String worldName = getWorldName(configuration.getString("location"));
        if (worldName != null && Bukkit.getWorld(worldName) == null) {
            var files = pendingHolograms.computeIfAbsent(worldName, w -> new ArrayList<>());
            files.add(file);
        }

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

    @Override
    public Hologram createHologram(HologramType hologramType, HologramConfiguration configuration, String fileName, String name, Location location) {

        String version = NmsVersion.getCurrentVersion().name().replace("V_", "v");
        String className = String.format("fr.maxlego08.essentials.nms.%s.CraftHologram", version);

        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(EssentialsPlugin.class, HologramType.class, HologramConfiguration.class, String.class, String.class, Location.class);
            return (Hologram) constructor.newInstance(this.plugin, hologramType, configuration, fileName, name, location);
        } catch (Exception exception) {
            this.plugin.getLogger().severe("Cannot create a new instance for the class " + className);
            this.plugin.getLogger().severe(exception.getMessage());
        }

        return null;
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event) {
        var key = event.getWorld().getName();
        if (this.pendingHolograms.containsKey(key)) {
            this.pendingHolograms.remove(key).forEach(this::loadHologram);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {

        if (!this.damageIndicator.enabled()) return;

        var damage = event.getFinalDamage();
        if (damage <= 0) return;

        var entity = event.getEntity();
        if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand) return;

        if (entity instanceof Player && !this.damageIndicator.players()) return;

        if (entity instanceof Animals && !this.damageIndicator.animals()) return;

        if (entity instanceof WaterMob && !this.damageIndicator.waterMobs()) return;

        if (entity instanceof Monster && !this.damageIndicator.mobs()) return;

        if (this.damageIndicator.disabledEntities().contains(entity.getType())) return;

        var location = entity.getLocation();
        location.add(0.0, this.damageIndicator.height(), 0.0);
        location = offsetLocation(location, this.damageIndicator.offsetX(), this.damageIndicator.offsetY(), this.damageIndicator.offsetZ());
        var critical = event instanceof EntityDamageByEntityEvent entityDamageByEntityEvent && entityDamageByEntityEvent.isCritical();
        var text = critical ? this.damageIndicator.criticalAppearance() : this.damageIndicator.appearance();
        text = getMessage(text, "%damage%", new DecimalFormat(this.damageIndicator.decimalFormat()).format(damage));

        HologramConfiguration hologramConfiguration = new TextHologramConfiguration();
        var hologram = createHologram(HologramType.TEXT, hologramConfiguration, "", "", location);

        hologram.addLine(new ZHologramLine(1, text));
        hologram.create();
        hologram.createForAllPlayers();

        this.plugin.getScheduler().runLater(hologram::deleteForAllPlayers, this.damageIndicator.duration());
    }
}
