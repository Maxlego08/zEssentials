package fr.maxlego08.essentials.storage;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.storage.storages.JsonStorage;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class ZStorageManager implements StorageManager {

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

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID playerUuid = event.getUniqueId();
        String playerName = event.getPlayerProfile().getName();

        this.iStorage.createOrLoad(playerUuid, playerName);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        this.iStorage.onPlayerQuit(event.getPlayer().getUniqueId());
    }
}
