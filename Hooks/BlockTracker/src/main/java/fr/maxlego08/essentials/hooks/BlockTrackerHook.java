package fr.maxlego08.essentials.hooks;

import dev.krakenied.blocktracker.bukkit.BukkitBlockTrackerPlugin;
import fr.maxlego08.essentials.api.block.BlockTracker;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class BlockTrackerHook implements BlockTracker {
    @Override
    public boolean isTracked(Block block) {
        return BukkitBlockTrackerPlugin.isTracked(block);
    }

    @Override
    public void track(Block block) {
        BukkitBlockTrackerPlugin plugin = (BukkitBlockTrackerPlugin) Bukkit.getPluginManager().getPlugin("BlockTracker");
        plugin.getTrackingManager().trackByBlock(block);
    }
}
