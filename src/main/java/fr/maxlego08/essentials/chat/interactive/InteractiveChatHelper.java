package fr.maxlego08.essentials.chat.interactive;

import fr.maxlego08.essentials.api.chat.InteractiveChat;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public abstract class InteractiveChatHelper implements Listener {

    protected Map<Player, InteractiveChat> interactiveChats = new HashMap<>();

    protected void onQuit(Player player) {
        this.interactiveChats.remove(player);
    }

    public void register(Player player, InteractiveChat interactiveChat) {
        this.interactiveChats.put(player, interactiveChat);
    }

    protected boolean onTalk(Player player, String message) {
        if (this.interactiveChats.containsKey(player)) {

            InteractiveChat interactiveChat = this.interactiveChats.remove(player);
            if (System.currentTimeMillis() >= interactiveChat.expiredAt()) return false;

            interactiveChat.consumer().accept(message);

            return true;
        }
        return false;
    }

}
