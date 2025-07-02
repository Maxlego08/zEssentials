package fr.maxlego08.essentials.api.nms;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface PlayerUtil {

    /**
     * Opens the ender chest of an offline player for the specified player.
     *
     * @param player the player who will view the ender chest
     * @param offlinePlayer the offline player whose ender chest is to be opened
     * @return true if the ender chest was successfully opened, false otherwise
     */
    boolean openEnderChest(Player player, OfflinePlayer offlinePlayer);

    /**
     * Opens the inventory of an offline player for the specified player.
     *
     * @param player the player who will view the inventory
     * @param offlinePlayer the offline player whose inventory is to be opened
     * @return true if the inventory was successfully opened, false otherwise
     */
    boolean openPlayerInventory(Player player, OfflinePlayer offlinePlayer);

}
