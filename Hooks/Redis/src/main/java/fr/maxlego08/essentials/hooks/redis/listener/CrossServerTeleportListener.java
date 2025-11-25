package fr.maxlego08.essentials.hooks.redis.listener;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.server.messages.CrossServerTeleport;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.teleport.CrossServerLocation;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.hooks.redis.RedisListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis listener for handling cross-server teleport requests.
 * When a player connects to this server after a cross-server teleport request,
 * they will be teleported to the destination.
 */
public class CrossServerTeleportListener extends RedisListener<CrossServerTeleport> {

    private final EssentialsPlugin plugin;
    // Store pending teleports for players who are connecting
    private static final Map<UUID, CrossServerTeleport> pendingTeleports = new ConcurrentHashMap<>();

    public CrossServerTeleportListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void onMessage(CrossServerTeleport message) {
        if (!message.isValid()) {
            return;
        }

        UUID playerUuid = message.getPlayerUuid();
        Player player = Bukkit.getPlayer(playerUuid);

        if (player != null && player.isOnline()) {
            // Player is already on this server, teleport them
            performTeleport(player, message);
        } else {
            // Player is not on this server yet, store for when they connect
            pendingTeleports.put(playerUuid, message);
            
            // Clean up after 30 seconds if player doesn't connect
            plugin.getScheduler().runLater(() -> pendingTeleports.remove(playerUuid), 30 * 20L);
        }
    }

    /**
     * Called when a player joins the server. Check if they have a pending teleport.
     */
    public static void onPlayerJoin(EssentialsPlugin plugin, Player player) {
        CrossServerTeleport pending = pendingTeleports.remove(player.getUniqueId());
        if (pending != null && pending.isValid()) {
            // Delay teleport slightly to ensure player is fully loaded
            plugin.getScheduler().runLater(() -> performTeleport(player, pending), 10L);
        }
    }

    private static void performTeleport(Player player, CrossServerTeleport message) {
        CrossServerLocation destination = message.getDestination();
        if (destination == null) return;

        World world = Bukkit.getWorld(destination.getWorld());
        if (world == null) {
            player.sendMessage("§cWorld not found: " + destination.getWorld());
            return;
        }

        Location location = new Location(world, destination.getX(), destination.getY(), destination.getZ(),
                destination.getYaw(), destination.getPitch());
        
        player.teleportAsync(location).thenAccept(success -> {
            if (success) {
                player.sendMessage("§aYou have been teleported!");
            } else {
                player.sendMessage("§cTeleportation failed!");
            }
        });
    }

    /**
     * Check if a player has a pending cross-server teleport.
     */
    public static boolean hasPendingTeleport(UUID playerUuid) {
        return pendingTeleports.containsKey(playerUuid);
    }

    /**
     * Clear a pending teleport for a player.
     */
    public static void clearPendingTeleport(UUID playerUuid) {
        pendingTeleports.remove(playerUuid);
    }
}

