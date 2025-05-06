package fr.maxlego08.essentials.listener.spigot;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends ZUtils implements Listener {

    private final EssentialsPlugin plugin;

    public ChatListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTalk(AsyncPlayerChatEvent event) {
        this.plugin.getSanctionManager().cancelChatEvent(event, event.getPlayer());
    }
}
