package fr.maxlego08.essentials.storage;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.storages.JsonStorage;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.UUID;

public class ZStorageManager extends ZUtils implements StorageManager {

    private final IStorage iStorage;
    private final EssentialsPlugin plugin;

    public ZStorageManager(EssentialsPlugin plugin) {
        this.plugin = plugin;
        StorageType storageType = plugin.getConfiguration().getStorageType();
        if (storageType == StorageType.MYSQL) {
            this.iStorage = new SqlStorage(plugin);
        } else {
            this.iStorage = new JsonStorage(plugin);
        }
    }

    @Override
    public void onEnable() {
        this.iStorage.onEnable();

        Bukkit.getOnlinePlayers().forEach(player -> this.iStorage.createOrLoad(player.getUniqueId(), player.getName()));
    }

    @Override
    public void onDisable() {
        this.iStorage.onDisable();
    }

    @Override
    public IStorage getStorage() {
        return this.iStorage;
    }

    @Override
    public StorageType getType() {
        return this.plugin.getStorageManager().getType();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        PlayerPreLoginEvent.Result result = event.getResult();
        if (result != PlayerPreLoginEvent.Result.ALLOWED) return;

        UUID playerUuid = event.getUniqueId();
        String playerName = event.getName();

        if (this.iStorage.isBan(playerUuid)) {
            Sanction sanction = this.iStorage.getBan(playerUuid);
            Duration duration = sanction.getDurationRemaining();
            this.plugin.getUtils().disallow(event, AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Message.MESSAGE_BAN_JOIN, "%reason%", sanction.getReason(), "%remaining%", TimerBuilder.getStringTime(duration.toMillis()));
            return;
        }

        User user = this.iStorage.createOrLoad(playerUuid, playerName);
        user.setAddress(event.getAddress().getHostAddress());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDisconnect(PlayerQuitEvent event) {
        this.iStorage.onPlayerQuit(event.getPlayer().getUniqueId());
    }
}
