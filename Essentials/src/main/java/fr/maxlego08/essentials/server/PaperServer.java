package fr.maxlego08.essentials.server;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PaperServer extends ZUtils implements EssentialsServer {

    private final EssentialsPlugin plugin;


    public PaperServer(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

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
            if (this.plugin.isFolia()) {
                player.kick(getComponentMessage(message, objects));
            } else {
                this.plugin.getScheduler().runAtLocation(player.getLocation(), wrappedTask -> player.kick(getComponentMessage(message, objects)));
            }
        }
    }

    @Override
    public boolean isOnline(String userName) {
        return Bukkit.getPlayer(userName) != null;
    }

    @Override
    public void clearChat(CommandSender sender) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            for (int i = 0; i != 300; i++) player.sendMessage(Component.text(""));
            message(player, Message.COMMAND_CHAT_CLEAR);
        });
        message(Bukkit.getConsoleSender(), Message.COMMAND_CHAT_CLEAR);
    }

    @Override
    public void toggleChat(boolean value) {
        ConfigStorage.chatDisable = value;
        ConfigStorage.getInstance().save(this.plugin.getPersist());
    }

    @Override
    public void broadcast(String message) {
        broadcast(Message.COMMAND_CHAT_BROADCAST, "%message%", message);
    }
}
