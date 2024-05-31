package fr.maxlego08.essentials.listener.paper;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.time.Duration;

public class ChatListener extends ZUtils implements Listener {

    private EssentialsPlugin plugin;

    public ChatListener(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTalk(AsyncChatEvent event) {

        Player player = event.getPlayer();
        IStorage iStorage = plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(player.getUniqueId());
        if (user != null && user.isMute()) {
            event.setCancelled(true);
            Sanction sanction = user.getMuteSanction();
            Duration duration = sanction.getDurationRemaining();
            message(player, Message.MESSAGE_MUTE_TALK, "%reason%", sanction.getReason(), "%duration%", TimerBuilder.getStringTime(duration.toMillis()));
        }
    }

}
