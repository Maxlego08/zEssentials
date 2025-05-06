package fr.maxlego08.essentials.hooks;

import com.nickuc.chat.api.events.PrivateMessageEvent;
import com.nickuc.chat.api.events.PublicMessageEvent;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NChatHook implements Listener {

    private final EssentialsPlugin plugin;

    public NChatHook(EssentialsPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPublicMessage(PublicMessageEvent event) {
        this.plugin.getSanctionManager().cancelChatEvent(event, event.getSender());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrivateMessage(PrivateMessageEvent event) {
        this.plugin.getSanctionManager().cancelChatEvent(event, event.getSender());
    }
}
