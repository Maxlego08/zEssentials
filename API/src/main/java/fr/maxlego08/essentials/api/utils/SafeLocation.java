package fr.maxlego08.essentials.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SafeLocation {

    private final String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
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

    public void setX(double x) {
        this.x = x;
        this.location = null;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        this.location = null;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
        this.location = null;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.location = null;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.location = null;
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
        return "SafeLocation{" + "world='" + world + '\'' + ", x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + ", location=" + location + '}';
    }

    public int getBlockX() {
        return (int) this.x;
    }

    public int getBlockY() {
        return (int) this.y;
    }

    public int getBlockZ() {
        return (int) this.z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = null;
    }
}
