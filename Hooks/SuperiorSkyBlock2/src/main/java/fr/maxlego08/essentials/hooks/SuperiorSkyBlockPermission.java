package fr.maxlego08.essentials.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import fr.maxlego08.essentials.api.permission.PermissionChecker;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SuperiorSkyBlockPermission implements PermissionChecker {
    @Override
    public boolean hasPermission(Player player, Block block) {

        var island = SuperiorSkyblockAPI.getIslandAt(block.getLocation());
        if (island == null) return false;

        var superiorPlayer = SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
        if (superiorPlayer == null) return false;

        return island.isMember(superiorPlayer) && island.isInsideRange(block.getLocation());
    }
}
