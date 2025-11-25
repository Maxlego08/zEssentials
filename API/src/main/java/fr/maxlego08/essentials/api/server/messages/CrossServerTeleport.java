package fr.maxlego08.essentials.api.server.messages;

import fr.maxlego08.essentials.api.teleport.CrossServerLocation;
import fr.maxlego08.essentials.api.teleport.TeleportType;

import java.util.UUID;

/**
 * Redis message for cross-server teleportation requests.
 */
public class CrossServerTeleport {

    private final UUID playerUuid;
    private final String playerName;
    private final TeleportType teleportType;
    private final CrossServerLocation destination;
    private final String targetName; // For TPA - the target player name
    private final long timestamp;

    public CrossServerTeleport(UUID playerUuid, String playerName, TeleportType teleportType, CrossServerLocation destination) {
        this(playerUuid, playerName, teleportType, destination, null);
    }

    public CrossServerTeleport(UUID playerUuid, String playerName, TeleportType teleportType, CrossServerLocation destination, String targetName) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.teleportType = teleportType;
        this.destination = destination;
        this.targetName = targetName;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public TeleportType getTeleportType() {
        return teleportType;
    }

    public CrossServerLocation getDestination() {
        return destination;
    }

    public String getTargetName() {
        return targetName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Check if this request is still valid (not expired).
     * Default timeout is 30 seconds.
     */
    public boolean isValid() {
        return System.currentTimeMillis() - timestamp < 30000;
    }
}

