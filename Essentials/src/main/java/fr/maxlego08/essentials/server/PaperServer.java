package fr.maxlego08.essentials.server;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PaperServer extends ZUtils implements EssentialsServer {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public List<String> getPlayersNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    @Override
    public void sendMessage(UUID uuid, Message message, Object... objects) {
        this.message(uuid, message, objects);
    }

    @Override
    public void broadcastMessage(Permission permission, Message message, Object... objects) {
        this.broadcast(permission, message, objects);
    }

    @Override
    public void kickPlayer(UUID uuid, Message message, Object... objects) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.kick(getComponentMessage(message, objects));
        }
    }

    @Override
    public boolean isOnline(String userName) {
        return Bukkit.getPlayer(userName) != null;
    }
}
