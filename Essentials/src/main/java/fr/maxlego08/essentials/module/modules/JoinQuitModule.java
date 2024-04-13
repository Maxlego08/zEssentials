package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.event.events.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.modules.joinquit.JoinQuitMessageType;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class JoinQuitModule extends ZModule {

    private boolean allowSilentJoinQuit;
    private JoinQuitMessageType customJoinMessage;
    private JoinQuitMessageType customQuitMessage;
    private boolean allowFirstJoinBroadcast;
    private boolean allowFirstJoinMotd;
    private int firstJoinMotdTicks;

    public JoinQuitModule(ZEssentialsPlugin plugin) {
        super(plugin, "join_quit");
    }

    @EventHandler
    public void onFirstJoin(UserFirstJoinEvent event) {

        if (!isEnable()) return;

        User user = event.getUser();

        if (!this.allowFirstJoinBroadcast) return;

        long totalUsers = this.plugin.getStorageManager().getStorage().totalUsers();
        EconomyProvider economyProvider = this.plugin.getEconomyProvider();

        Bukkit.getOnlinePlayers().forEach(player -> {
            message(player, Message.FIRST_JOIN_MESSAGE, "%player%", user.getName(), "%totalUser%", totalUsers, "%totalUserFormat%", economyProvider.format(totalUsers));
        });
        message(Bukkit.getConsoleSender(), Message.FIRST_JOIN_MESSAGE, "%player%", user.getName(), "%totalUser%", totalUsers, "%totalUserFormat%", economyProvider.format(totalUsers));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {

        if (!isEnable()) return;

        Player player = event.getPlayer();
        User user = getUser(player);

        if (this.allowSilentJoinQuit && hasPermission(player, Permission.ESSENTIALS_SILENT_JOIN)) {
            event.joinMessage(Component.empty());
        } else if (this.customJoinMessage == JoinQuitMessageType.DISABLE) {
            event.joinMessage(Component.empty());
        } else if (this.customJoinMessage == JoinQuitMessageType.CUSTOM) {
            event.joinMessage(componentMessage.getComponent(getMessage(Message.JOIN_MESSAGE, "%player%", player.getName(), "%displayName%", player.getDisplayName())));
        }

        if (user != null && user.isFirstJoin() && this.allowFirstJoinMotd) {
            this.plugin.getScheduler().runAtLocationLater(player.getLocation(), () -> {
                message(player, Message.FIRST_JOIN_MOTD, "%player%", player.getName(), "%displayName%", player.getDisplayName());
            }, this.firstJoinMotdTicks);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {

        if (!isEnable()) return;

        Player player = event.getPlayer();

        if (this.allowSilentJoinQuit && hasPermission(player, Permission.ESSENTIALS_SILENT_QUIT)) {
            event.quitMessage(Component.empty());
        } else if (this.customJoinMessage == JoinQuitMessageType.DISABLE) {
            event.quitMessage(Component.empty());
        } else if (this.customJoinMessage == JoinQuitMessageType.CUSTOM) {
            event.quitMessage(componentMessage.getComponent(getMessage(Message.QUIT_MESSAGE, "%player%", player.getName(), "%displayName%", player.getDisplayName())));
        }
    }
}
