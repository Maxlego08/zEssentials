package fr.maxlego08.essentials.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SafeLocation {

    private final String world;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private transient Location location;

    public SafeLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SafeLocation(Location location) {
        this(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.location = location;
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

    public Location getLocation() {
        if (this.location == null) {
            this.location = new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
        }
        return location;
    }

    public boolean isValid() {
        return this.world != null && Bukkit.getWorld(this.world) != null;
    }

    @Override
    public String toString() {
        return "SafeLocation{" +
                "world='" + world + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", location=" + location +
                '}';
    }
}
