package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.storage.ConfigStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Folia-safe helpers for player join flows.
 * <p>
 * Teleports must not run during {@link org.bukkit.event.player.PlayerJoinEvent}
 * or {@link org.bukkit.event.player.PlayerLoginEvent}; the player is still being placed in the world.
 */
public final class FoliaJoinHelper {

    private static final double SPAWN_TOLERANCE_SQUARED = 1.0;
    private static final long JOIN_TELEPORT_DELAY_TICKS = 1L;

    private FoliaJoinHelper() {
    }

    public static Location resolveFirstSpawnLocation() {
        if (ConfigStorage.firstSpawnLocation != null && ConfigStorage.firstSpawnLocation.isValid()) {
            return ConfigStorage.firstSpawnLocation.getLocation();
        }
        if (ConfigStorage.spawnLocation != null && ConfigStorage.spawnLocation.isValid()) {
            return ConfigStorage.spawnLocation.getLocation();
        }
        return null;
    }

    public static void teleportFirstSpawnAfterJoin(EssentialsPlugin plugin, Player player) {
        teleportAfterJoin(plugin, player, resolveFirstSpawnLocation(), true);
    }

    public static void teleportAfterJoin(EssentialsPlugin plugin, Player player, Location target) {
        teleportAfterJoin(plugin, player, target, false);
    }

    public static void teleportAfterJoin(EssentialsPlugin plugin, Player player, Location target, boolean skipWhenAlreadyThere) {
        if (player == null || target == null || target.getWorld() == null) {
            return;
        }

        Location destination = target;
        plugin.getScheduler().runAtLocationLater(player.getLocation(), () -> {
            if (!player.isOnline()) {
                return;
            }

            if (skipWhenAlreadyThere) {
                Location current = player.getLocation();
                if (current.getWorld().equals(destination.getWorld())
                        && current.distanceSquared(destination) < SPAWN_TOLERANCE_SQUARED) {
                    return;
                }
            }

            plugin.getScheduler().teleportAsync(player, destination);
        }, JOIN_TELEPORT_DELAY_TICKS);
    }
}
