package fr.maxlego08.essentials.commands.commands.weather;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.nms.PlayerUtil;
import fr.maxlego08.menu.zcore.utils.nms.NmsVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for smooth time transitions in worlds and for players
 */
class TimeTransition {

    private static final long WORLD_TIME_STEP = 150;

    static final Set<UUID> TIME_CHANGING_WORLDS = ConcurrentHashMap.newKeySet();

    /**
     * Smoothly transitions world time to target time
     */
    static void transitionWorldTime(EssentialsPlugin plugin, World world, long targetTime) {
        UUID worldId = world.getUID();
        TIME_CHANGING_WORLDS.add(worldId);

        long startTime = world.getFullTime();
        long diff = (targetTime - startTime + 24000) % 24000;

        new BukkitRunnable() {
            long progressed = 0;

            @Override
            public void run() {
                if (progressed >= diff) {
                    world.setFullTime(targetTime);
                    TIME_CHANGING_WORLDS.remove(worldId);
                    cancel();
                    return;
                }

                world.setFullTime(world.getFullTime() + WORLD_TIME_STEP);
                progressed += WORLD_TIME_STEP;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * Checks if world time is currently being changed
     */
    static boolean isWorldTimeChanging(UUID worldId) {
        return TIME_CHANGING_WORLDS.contains(worldId);
    }
}
