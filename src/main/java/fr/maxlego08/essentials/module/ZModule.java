package fr.maxlego08.essentials.module;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.event.events.economy.EconomyBaltopUpdateEvent;
import fr.maxlego08.essentials.api.modules.Module;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.YamlLoader;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.pattern.PatternManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.PluginManager;

import java.io.File;
import java.util.List;
import java.util.UUID;

public abstract class ZModule extends YamlLoader implements Module {

    protected final ZEssentialsPlugin plugin;
    protected final String name;
    protected boolean isEnable = false;
    protected boolean isRegisterEvent = true;
    protected boolean copyAndUpdate = true;

    public ZModule(ZEssentialsPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    @Override
    public void loadConfiguration() {

        File folder = getFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (this.copyAndUpdate) {
            this.plugin.saveOrUpdateConfiguration("modules/" + this.name + "/config.yml", true);
        } else {
            File file = new File(getFolder(), "config.yml");
            if (!file.exists()) {
                this.plugin.saveResource("modules/" + this.name + "/config.yml", false);
            }
        }

        YamlConfiguration configuration = getConfiguration();
        this.loadYamlConfirmation(this.plugin, configuration);

        this.isEnable = configuration.getBoolean("enable", true);
    }

    @Override
    public boolean isRegisterEvent() {
        return isRegisterEvent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public File getFolder() {
        return new File(this.plugin.getDataFolder(), "modules/" + name);
    }

    @Override
    public YamlConfiguration getConfiguration() {
        return YamlConfiguration.loadConfiguration(new File(getFolder(), "config.yml"));
    }

    @Override
    public boolean isEnable() {
        return this.isEnable;
    }

    protected void loadInventory(String inventoryName) {
        InventoryManager inventoryManager = this.plugin.getInventoryManager();

        try {
            inventoryManager.loadInventoryOrSaveResource(this.plugin, "modules/" + name + "/" + inventoryName + ".yml");
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
    }

    protected void loadPattern(String patternName) {
        PatternManager patternManager = this.plugin.getPatternManager();

        File file = new File(this.plugin.getDataFolder(), "patterns/" + patternName + ".yml");
        if (!file.exists()) {
            this.plugin.saveResource("patterns/" + patternName + ".yml", false);
        }

        try {
            patternManager.loadPattern(file);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
    }

    protected void savePattern(String patternName) {
        File file = new File(this.plugin.getDataFolder(), "patterns/" + patternName + ".yml");
        if (!file.exists()) {
            this.plugin.saveResource("patterns/" + patternName + ".yml", false);
        }
    }

    protected User getUser(Entity entity) {
        return this.plugin.getStorageManager().getStorage().getUser(entity.getUniqueId());
    }

    protected boolean isPaperVersion() {
        try {
            Class.forName("net.kyori.adventure.text.Component");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isCopyAndUpdate() {
        return copyAndUpdate;
    }

    /**
     * Registers a list of event classes by their names with the plugin manager.
     *
     * @param events a list of event class names to be registered
     */
    protected void registerEvents(List<String> events) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        events.forEach(eventName -> {

            try {
                Class<?> eventClass = Class.forName(eventName);

                if (!Event.class.isAssignableFrom(eventClass)) {
                    this.plugin.getLogger().severe("Class " + eventName + " is not an event !");
                    return;
                }

                pluginManager.registerEvent(eventClass.asSubclass(Event.class), this, EventPriority.NORMAL, (listener, event) -> updateLineWithEvent(eventName, event), this.plugin);
            } catch (ClassNotFoundException exception) {
                this.plugin.getLogger().severe("Class " + eventName + " was not found !");
                exception.printStackTrace();
            }
        });
    }

    /**
     * Updates the event handling logic for the specified event.
     *
     * @param eventName the name of the event
     * @param event     the event object
     */
    private void updateLineWithEvent(String eventName, Event event) {
        if (event instanceof PlayerEvent playerEvent) {
            updateEventPlayer(playerEvent.getPlayer(), eventName);
        } else if (event instanceof BlockBreakEvent playerEvent) {
            updateEventPlayer(playerEvent.getPlayer(), eventName);
        } else if (event instanceof BlockPlaceEvent playerEvent) {
            updateEventPlayer(playerEvent.getPlayer(), eventName);
        } else if (event instanceof UserEvent userEvent) {
            UUID uuid = userEvent.getUser().getUniqueId();
            updateEventUniqueId(uuid, eventName);
        } else if (event instanceof EconomyBaltopUpdateEvent) {
            updateEvent(eventName);
        } else {
            this.plugin.getLogger().severe("Event : " + eventName + " is not a Player or User event ! You cant use it");
        }
    }

    /**
     * Updates the player-related event handling logic.
     *
     * @param player    the Player involved in the event
     * @param eventName the name of the event
     */
    protected void updateEventPlayer(Player player, String eventName) {

    }

    /**
     * Updates the UUID-related event handling logic.
     *
     * @param uniqueId  the UUID involved in the event
     * @param eventName the name of the event
     */
    protected void updateEventUniqueId(UUID uniqueId, String eventName) {

    }

    /**
     * Updates the event handling logic for the specified event name.
     *
     * @param eventName the name of the event to update
     */
    protected void updateEvent(String eventName) {
        Bukkit.getOnlinePlayers().forEach(player -> updateEventPlayer(player, eventName));
    }

    protected <T> T or(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
