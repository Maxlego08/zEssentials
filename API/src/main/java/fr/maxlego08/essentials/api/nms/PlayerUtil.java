package fr.maxlego08.essentials.api.nms;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface PlayerUtil {

    boolean openEnderChest(Player player, OfflinePlayer offlinePlayer);

    boolean openPlayerInventory(Player player, OfflinePlayer offlinePlayer);

}
