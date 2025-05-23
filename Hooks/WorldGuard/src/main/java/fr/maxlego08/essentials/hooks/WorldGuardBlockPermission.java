package fr.maxlego08.essentials.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.maxlego08.essentials.api.permission.PermissionChecker;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardBlockPermission implements PermissionChecker {
    @Override
    public boolean hasPermission(Player player, Block block) {

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        LocalPlayer localPlayer = new BukkitPlayer(WorldGuardPlugin.inst(), player);
        var result = query.testBuild(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BLOCK_BREAK);
        System.out.println(result + " -> " + localPlayer);

        return result;
    }
}
