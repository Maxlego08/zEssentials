package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class SpawnModule extends ZModule {

    private String respawnListenerPriority;
    private String spawnJoinListenerPriority;
    private boolean respawnAtAnchor;
    private boolean respawnAtHome;
    private boolean respawnAtBed;

    public SpawnModule(ZEssentialsPlugin plugin) {
        super(plugin, "spawn");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        if (!this.isEnable()) return;

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        EventPriority respawnPriority = getPriority(this.respawnListenerPriority);
        HandlerList.unregisterAll(this);

        if (respawnPriority != null) {
            pluginManager.registerEvent(PlayerRespawnEvent.class, this, respawnPriority, (listener, event) -> {
                if (listener instanceof SpawnModule spawnModule && event instanceof PlayerRespawnEvent playerRespawnEvent) {
                    spawnModule.onRespawn(playerRespawnEvent, playerRespawnEvent.getPlayer());
                }
            }, this.plugin);
        }

        EventPriority spawnPriority = getPriority(this.spawnJoinListenerPriority);
        pluginManager.registerEvent(PlayerSpawnLocationEvent.class, this, spawnPriority, (listener, event) -> {
            if (listener instanceof SpawnModule spawnModule && event instanceof PlayerSpawnLocationEvent playerSpawnLocationEvent) {
                spawnModule.onSpawnLocation(playerSpawnLocationEvent, playerSpawnLocationEvent.getPlayer());
            }
        }, this.plugin);

        if (this.plugin.isFolia()) {
            pluginManager.registerEvent(PlayerDeathEvent.class, this, EventPriority.LOWEST, (listener, event) -> {
                if (listener instanceof SpawnModule spawnModule && event instanceof PlayerDeathEvent playerDeathEvent) {
                    spawnModule.onPlayerDeath(playerDeathEvent, playerDeathEvent.getPlayer());
                }
            }, this.plugin);
        }
    }

    private void onPlayerDeath(PlayerDeathEvent playerDeathEvent, Player player) {

        Location respawnLocation = player.getRespawnLocation();

        if (this.respawnAtAnchor && respawnLocation != null) return;

        if (this.respawnAtBed) {
            // ToDo
        }

        if (this.respawnAtHome) {
            // ToDo
        }

        if (ConfigStorage.spawnLocation != null) {
            player.setRespawnLocation(ConfigStorage.spawnLocation, true);
        }
    }

    public void onSpawnLocation(PlayerSpawnLocationEvent event, Player player) {
        User user = getUser(player);
        if (user != null && user.isFirstJoin() && ConfigStorage.spawnLocation != null) {
            event.setSpawnLocation(ConfigStorage.spawnLocation);
        }
    }

    private void onRespawn(PlayerRespawnEvent event, Player player) {

        // If the spawn does not exist, we must do nothing
        if (ConfigStorage.spawnLocation == null) return;

        if (event.isAnchorSpawn() && respawnAtAnchor) {
            return;
        }

        if (this.respawnAtHome) {
            // ToDo - Use home

        }

        if (this.respawnAtBed && event.isBedSpawn()) {
            Location respawnLocation = player.getRespawnLocation();
            if (respawnLocation != null) {
                event.setRespawnLocation(respawnLocation);
                return;
            }
        }

        event.setRespawnLocation(ConfigStorage.spawnLocation);
    }
}
