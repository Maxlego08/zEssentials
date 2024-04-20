package fr.maxlego08.essentials.zutils.utils.spigot;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.BaseServer;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.UUID;

public class SpigotUtils extends BaseServer {


    public SpigotUtils(EssentialsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void kick(Player player, Message message, Object... objects) {
        Bukkit.getScheduler().runTask(this.plugin, () -> player.kickPlayer(getMessage(message, objects)));
    }

    @Override
    public void disallow(AsyncPlayerPreLoginEvent event, AsyncPlayerPreLoginEvent.Result result, Message message, Object... objects) {
        event.disallow(result, getMessage(message, objects));
    }
}
