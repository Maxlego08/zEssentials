package fr.maxlego08.essentials;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Inert Spigot entry point. Paper selects paper-plugin.yml first and never enables this class.
 * Does not reference any real zEssentials, zMenu or Paper-only classes.
 */
public final class ZEssentialsSpigotFallback extends JavaPlugin implements Listener {

    private static final String WARNING_PERMISSION = "zessentials.paper.warning";
    private static final String DOWNLOAD_URL = "https://papermc.io/downloads";
    private static final long JOIN_MESSAGE_DELAY = 40L;

    @Override
    public void onEnable() {
        this.getLogger().warning("*************************************************************");
        this.getLogger().warning("zEssentials is inactive: no commands, modules or API are available on Spigot.");
        this.getLogger().warning("Install Paper, or a compatible fork, to use zEssentials.");
        this.getLogger().warning("Download: " + DOWNLOAD_URL);
        this.getLogger().warning("*************************************************************");
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(WARNING_PERMISSION)) return;

        this.getServer().getScheduler().runTaskLater(this, () -> {
            if (!player.isOnline()) return;
            player.sendMessage("zEssentials is not running");
            player.sendMessage("This server runs Spigot, zEssentials requires Paper.");
            player.sendMessage("Download Paper: " + DOWNLOAD_URL);
        }, JOIN_MESSAGE_DELAY);
    }
}
