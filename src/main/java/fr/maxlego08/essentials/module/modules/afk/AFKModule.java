package fr.maxlego08.essentials.module.modules.afk;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.afk.AfkManager;
import fr.maxlego08.essentials.api.afk.AfkPermission;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.Permissible;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {

            if (hasPermission(onlinePlayer, Permission.ESSENTIALS_AFK_BYPASS)) continue;

            var user = getUser(onlinePlayer);
            if (user == null) continue;
            checkUser(user);
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
        return permissions.stream().filter(permission -> permissible.hasPermission(permission.permission())).max(Comparator.comparingInt(AfkPermission::priority));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {

        var user = getUser(event.getPlayer());
        if (user == null) return;

        var from = event.getFrom();
        var to = event.getTo();

        if (from.getYaw() != to.getYaw() || from.getPitch() != to.getPitch()) {
            user.setLastActiveTime();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        var user = getUser(event.getPlayer());
        if (user == null) return;

        user.setLastActiveTime();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTalk(AsyncPlayerChatEvent event) {
        var user = getUser(event.getPlayer());
        if (user == null) return;

        user.setLastActiveTime();
    }
}
