package fr.maxlego08.essentials.module.modules.afk;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.afk.AfkManager;
import fr.maxlego08.essentials.api.afk.AfkPermission;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class AFKModule extends ZModule implements AfkManager {

    private WrappedTask wrappedTask;
    @NonLoadable
    private List<Action> actions = new ArrayList<>();
    private int checkInterval;
    private List<AfkPermission> permissions;
    private boolean softKick;
    private String softKickMessage;
    private String placeholderAfk;
    private String placeholderNotAfk;
    // Performance optimization - Process players in batches to prevent server freezes
    private static final int BATCH_SIZE = 20;
    private Iterator<? extends Player> playerIterator;

    public AFKModule(ZEssentialsPlugin plugin) {
        super(plugin, "afk");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        var configuration = getConfiguration();
        List<Map<String, Object>> elements = (List<Map<String, Object>>) configuration.getList("kick-actions", new ArrayList<>());
        this.actions = this.plugin.getButtonManager().loadActions(elements, "kick-actions", getConfigurationFile());

        if (this.wrappedTask != null) this.wrappedTask.cancel();

        if (checkInterval > 0) {

            this.wrappedTask = this.plugin.getScheduler().runTimer(this::checkPlayers, checkInterval, checkInterval);
        }
    }

    private void checkPlayers() {
        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
        
        // Initialize new iterator or continue with existing one
        if (playerIterator == null || !playerIterator.hasNext()) {
            playerIterator = onlinePlayers.iterator();
        }
        
        // Process only BATCH_SIZE players per check to avoid performance spikes
        int processed = 0;
        while (playerIterator.hasNext() && processed < BATCH_SIZE) {
            Player onlinePlayer = playerIterator.next();
            
            // Skip offline players and those with bypass permission
            if (!onlinePlayer.isOnline()) continue;
            if (hasPermission(onlinePlayer, Permission.ESSENTIALS_AFK_BYPASS)) continue;

            var user = getUser(onlinePlayer);
            if (user == null) continue;
            checkUser(user);
            
            processed++;
        }
    }

    @Override
    public void checkUser(User user) {

        var optional = getPermission(user.getPlayer());
        if (optional.isEmpty()) return;

        var permission = optional.get();
        var difference = (System.currentTimeMillis() - user.getLastActiveTime()) / 1000;

        var component = this.plugin.getComponentMessage();
        var player = user.getPlayer();

        if (difference >= permission.maxAfkTime()) {

            var fakeInventory = this.plugin.getInventoryManager().getFakeInventory();
            for (Action action : actions) {
                action.preExecute(player, null, fakeInventory, new Placeholders());
            }

            if (softKick) {
                component.kick(player, papi(this.softKickMessage, player));
            }
        } else if (difference == permission.startAfkTime() && permission.messageOnStartAfk() != null) {
            component.sendMessage(player, papi(permission.messageOnStartAfk(), player));
        }
    }

    @Override
    public Optional<AfkPermission> getPermission(Permissible permissible) {
        AfkPermission selected = null;
        for (AfkPermission permission : permissions) {
            if (permissible.hasPermission(permission.permission())) {
                if (selected == null || permission.priority() > selected.priority()) {
                    selected = permission;
                }
            }
        }
        return Optional.ofNullable(selected);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {

        var from = event.getFrom();
        var to = event.getTo();

        // Only process if there's actual movement
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ() 
            && from.getYaw() == to.getYaw() && from.getPitch() == to.getPitch()) {
            return;
        }

        var user = getUser(event.getPlayer());
        if (user == null) return;

        endAfk(user);
        user.setLastActiveTime();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var user = getUser(event.getPlayer());
        if (user == null) return;

        String message = event.getMessage().toLowerCase(Locale.ENGLISH);
        if (message.startsWith("/")) {
            message = message.substring(1);
        }
        if (message.equals("afk") || message.startsWith("afk ")) {
            return;
        }

        endAfk(user);
        user.setLastActiveTime();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTalk(AsyncPlayerChatEvent event) {
        var user = getUser(event.getPlayer());
        if (user == null) return;

        endAfk(user);
        user.setLastActiveTime();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        var user = getUser(event.getWhoClicked());
        if (user == null) return;

        endAfk(user);
        user.setLastActiveTime();
    }

    private void endAfk(User user) {

        if (!user.isAfk()) return;

        if (user.isManualAfk()) {
            user.setAfk(false);
        }

        var lastActiveTime = user.getLastActiveTime();

        var optional = getPermission(user.getPlayer());
        if (optional.isEmpty()) return;

        var permission = optional.get();
        if (permission.messageOnEndAfk() != null) {
            var component = this.plugin.getComponentMessage();
            Placeholders placeholders = new Placeholders();
            placeholders.register("duration", TimerBuilder.getStringTime(System.currentTimeMillis() - lastActiveTime));
            component.sendMessage(user.getPlayer(), papi(placeholders.parse(permission.messageOnEndAfk()), user.getPlayer()));
        }
    }

    @Override
    public String getPlaceholderAfk() {
        return this.placeholderAfk == null ? "&aV" : this.placeholderAfk;
    }

    @Override
    public String getPlaceholderNotAfk() {
        return this.placeholderNotAfk == null ? "&cX" : this.placeholderNotAfk;
    }
}
