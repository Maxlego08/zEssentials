package fr.maxlego08.essentials.storage;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.exception.UserBanException;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.storage.StorageManager;
import fr.maxlego08.essentials.api.storage.StorageType;
import fr.maxlego08.essentials.storage.storages.JsonStorage;
import fr.maxlego08.essentials.storage.storages.SqlStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        Bukkit.getOnlinePlayers().forEach(player -> {
            try {
                this.iStorage.createOrLoad(player.getUniqueId(), player.getName());
            } catch (UserBanException ignored) {
            }
        });
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

        try {
            this.iStorage.createOrLoad(playerUuid, playerName);
        } catch (UserBanException exception) {
            SanctionDTO sanctionDTO = exception.getSanctionDTO();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, getComponentMessage(Message.MESSAGE_BAN_JOIN, "%reason%", sanctionDTO.reason()));
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        this.iStorage.onPlayerQuit(event.getPlayer().getUniqueId());
    }
}
