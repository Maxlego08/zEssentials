package fr.maxlego08.essentials.nms.v1_21;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.nms.PlayerUtil;
import fr.maxlego08.essentials.nms.v1_21.enderchest.PlayerManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils implements PlayerUtil {

    private final EssentialsPlugin plugin;

    public PlayerUtils(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void openEncherChest(Player player, OfflinePlayer offlinePlayer) {

        PlayerManager playerManager = new PlayerManager(plugin.getLogger());
        var loadPlayer = playerManager.loadPlayer(offlinePlayer);
        System.out.println(loadPlayer);

        if (loadPlayer != null) player.openInventory(loadPlayer.getEnderChest());
        else plugin.getLogger().info("Aie aie aie");
    }

}
