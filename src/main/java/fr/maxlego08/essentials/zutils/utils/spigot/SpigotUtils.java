package fr.maxlego08.essentials.zutils.utils.spigot;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.BaseServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class SpigotUtils extends BaseServer {


    public SpigotUtils(EssentialsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void kick(Player player, Message message, Object... objects) {
        Bukkit.getScheduler().runTask(this.plugin, () -> player.kickPlayer(getMessage(message, objects)));
    }

    @Override
    public void disallow(PlayerLoginEvent event, PlayerLoginEvent.Result result, Message message, Object... objects) {
        event.disallow(result, getMessage(message, objects));
    }

    @Override
    public void sendPrivateMessage(User user, PrivateMessage privateMessage, Message message, String content) {
        message(user, message, "%target%", privateMessage.username(), "%message%", content);
    }
}
