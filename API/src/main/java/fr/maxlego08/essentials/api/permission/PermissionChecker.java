package fr.maxlego08.essentials.api.permission;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface PermissionChecker {

    /**
     * Checks if a player has permission on a block.
     *
     * @param player The player to check
     * @param block The block to check
     * @return true if the player has permission, false otherwise
     */
    boolean hasPermission(Player player, Block block);

}
