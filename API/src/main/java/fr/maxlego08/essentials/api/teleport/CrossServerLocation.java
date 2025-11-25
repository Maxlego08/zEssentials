package fr.maxlego08.essentials.api.teleport;

import fr.maxlego08.essentials.api.utils.SafeLocation;

/**
 * Represents a location that can span across multiple servers.
 * Used for cross-server teleportation via Redis/BungeeCord.
 */
public class CrossServerLocation {

    private final String serverName;
    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public CrossServerLocation(String serverName, String world, double x, double y, double z, float yaw, float pitch) {
        this.serverName = serverName;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public CrossServerLocation(String serverName, SafeLocation location) {
        this(serverName, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public String getServerName() {
        return serverName;
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public SafeLocation toSafeLocation() {
        return new SafeLocation(world, x, y, z, yaw, pitch);
    }

    /**
     * Checks if this location is on the same server.
     *
     * @param currentServer The current server name
     * @return true if the location is on the same server
     */
    public boolean isSameServer(String currentServer) {
        return serverName == null || serverName.isEmpty() || serverName.equalsIgnoreCase(currentServer);
    }

    @Override
    public String toString() {
        return "CrossServerLocation{" +
                "serverName='" + serverName + '\'' +
                ", world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}

