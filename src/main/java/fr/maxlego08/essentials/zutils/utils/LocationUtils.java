package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.utils.SafeLocation;
import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

public abstract class LocationUtils {

    protected Location offsetLocation(Location location, double x, double y, double z) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return location.clone().add(random.nextDouble(-x, x), random.nextDouble(-y, y), random.nextDouble(-z, z));
    }

    public String locationAsString(SafeLocation location) {
        return location == null ? null : String.join(",",
                location.getWorld(),
                String.valueOf(location.getX()),
                String.valueOf(location.getY()),
                String.valueOf(location.getZ()),
                String.valueOf(location.getYaw()),
                String.valueOf(location.getPitch())
        );
    }

    public String locationAsString(Location location) {
        return locationAsString(new SafeLocation(location));
    }

    protected String getWorldName(String string) {
        if (string == null) return null;
        String[] parts = string.split(",");
        return parts.length == 0 ? null : parts[0];
    }

    public SafeLocation stringAsLocation(String string) {
        if (string == null) return null;
        String[] parts = string.split(",");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid location string: " + string);
        }
        String world = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = parts.length > 4 ? Float.parseFloat(parts[4]) : 0.0f;
        float pitch = parts.length > 5 ? Float.parseFloat(parts[5]) : 0.0f;

        return new SafeLocation(world, x, y, z, yaw, pitch);
    }
}
