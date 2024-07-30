package fr.maxlego08.essentials.server;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.essentials.zutils.utils.paper.PaperComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
    public List<String> getOfflinePlayersNames() {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
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
    public void broadcastMessage(Option option, Message message, Object... objects) {
        this.plugin.getUtils().broadcast(option, message, objects);
    }

    @Override
    public void kickPlayer(UUID uuid, Message message, Object... objects) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            PaperComponent paperComponent = (PaperComponent) this.componentMessage;
            if (this.plugin.isFolia()) {
                player.kick(paperComponent.getComponentMessage(message, objects));
            } else {
                this.plugin.getScheduler().runAtLocation(player.getLocation(), wrappedTask -> player.kick(paperComponent.getComponentMessage(message, objects)));
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
        ConfigStorage.chatEnable = value;
        ConfigStorage.getInstance().save(this.plugin.getPersist());
    }

    @Override
    public void broadcast(String message) {
        broadcast(Message.COMMAND_CHAT_BROADCAST, "%message%", message);
    }

    @Override
    public void sendPrivateMessage(User user, PrivateMessage privateMessage, String message) {
        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User targetUser = iStorage.getUser(privateMessage.uuid());
        if (targetUser == null) return;

        PrivateMessage privateMessageReply = targetUser.setPrivateMessage(user.getUniqueId(), user.getName());
        this.plugin.getUtils().sendPrivateMessage(targetUser, privateMessageReply, Message.COMMAND_MESSAGE_OTHER, message);
    }

    @Override
    public void deleteCooldown(UUID uniqueId, String cooldownName) {
        this.plugin.getUtils().deleteCooldown(uniqueId, cooldownName);
    }

    @Override
    public void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt) {
        this.plugin.getUtils().updateCooldown(uniqueId, cooldownName, expiredAt);
    }
}
