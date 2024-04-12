package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;

public class SpawnModule extends ZModule {

    private String respawnListenerPriority;
    private String spawnJoinListenerPriority;

    public SpawnModule(ZEssentialsPlugin plugin) {
        super(plugin, "spawn");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();


        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        EventPriority respawnPriority = getPriority(this.respawnListenerPriority);
        System.out.println(respawnPriority);
        if (respawnPriority != null) {
            pluginManager.registerEvent(PlayerRespawnEvent.class, this, respawnPriority, (listener, event) -> {
                System.out.println("L " + listener);
                System.out.println("E " + event);
                if (listener instanceof SpawnModule spawnModule && event instanceof PlayerRespawnEvent playerRespawnEvent){
                    spawnModule.onRespawn(playerRespawnEvent, playerRespawnEvent.getPlayer());
                }
            }, this.plugin);
        }
    }

    private void onRespawn(PlayerRespawnEvent playerRespawnEvent, Player player) {

    }
}
