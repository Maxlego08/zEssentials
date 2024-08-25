package fr.maxlego08.essentials.nms.v1_20_4;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.nms.PlayerUtil;
import fr.maxlego08.essentials.api.utils.inventory.EnderChestHolder;
import fr.maxlego08.essentials.api.utils.inventory.PlayerInventoryHolder;
import fr.maxlego08.essentials.nms.v1_20_4.enderchest.CraftPlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils implements PlayerUtil {

    private final EssentialsPlugin plugin;

    public PlayerUtils(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean openEnderChest(Player player, OfflinePlayer offlinePlayer) {

        CraftPlayerManager craftPlayerManager = new CraftPlayerManager(plugin.getLogger());
        var loadPlayer = craftPlayerManager.loadPlayer(offlinePlayer);

        if (loadPlayer != null) {
            EnderChestHolder enderChestHolder = new EnderChestHolder(loadPlayer);
            player.openInventory(enderChestHolder.getInventory());
            return true;
        }
        return false;
    }

    @Override
    public boolean openPlayerInventory(Player player, OfflinePlayer offlinePlayer) {
        CraftPlayerManager craftPlayerManager = new CraftPlayerManager(plugin.getLogger());
        var loadPlayer = craftPlayerManager.loadPlayer(offlinePlayer);

        if (loadPlayer != null) {
            PlayerInventoryHolder enderChestHolder = new PlayerInventoryHolder(loadPlayer);
            player.openInventory(enderChestHolder.getInventory());
            return true;
        }
        return false;
    }
}
