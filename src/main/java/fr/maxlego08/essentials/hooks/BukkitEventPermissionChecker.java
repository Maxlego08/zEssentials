package fr.maxlego08.essentials.hooks;

import fr.maxlego08.essentials.api.permission.PermissionChecker;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class BukkitEventPermissionChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(Player player, Block block) {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);
        return !event.isCancelled();
    }
}
