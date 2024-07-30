package fr.maxlego08.essentials.chat.interactive;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class InteractiveChatPaperListener extends InteractiveChatHelper {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {

        String message = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());
        if (this.onTalk(event.getPlayer(), message)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.onQuit(event.getPlayer());
    }

}
