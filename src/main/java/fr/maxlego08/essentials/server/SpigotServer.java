package fr.maxlego08.essentials.server;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.teleport.CrossServerLocation;
import fr.maxlego08.essentials.api.teleport.TeleportType;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.storage.ConfigStorage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpigotServer implements EssentialsServer {

    private final EssentialsPlugin plugin;

    public SpigotServer(EssentialsPlugin plugin) {
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
        return this.plugin.getConfiguration().isEnableOfflinePlayersName() ? Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList() : getPlayersNames();
    }

    @Override
    public void sendMessage(UUID uuid, Message message, Object... objects) {

    }

    @Override
    public void broadcastMessage(Permission permission, Message message, Object... objects) {

    }

    @Override
    public void broadcastMessage(Option option, Message message, Object... objects) {

    }

    @Override
    public void kickPlayer(UUID uuid, Message message, Object... objects) {

    }

    @Override
    public boolean isOnline(String userName) {
        return Bukkit.getPlayer(userName) != null;
    }

    @Override
    public void clearChat(CommandSender sender) {
    }

    @Override
    public void toggleChat(boolean value) {
        ConfigStorage.chatEnable = value;
        ConfigStorage.getInstance().save(this.plugin.getPersist());
    }

    @Override
    public void broadcast(String message) {

    }

    @Override
    public void pub(Player player, String message) {

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
        this.plugin.getUtils().deleteCooldown(uniqueId, cooldownName);
    }

    @Override
    public void sendToServer(Player player, String serverName) {
        try {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArray);
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            player.sendPluginMessage(this.plugin, "BungeeCord", byteArray.toByteArray());
        } catch (IOException exception) {
            this.plugin.getLogger().severe("Failed to send player to server: " + exception.getMessage());
        }
    }

    @Override
    public void crossServerTeleport(Player player, TeleportType teleportType, CrossServerLocation destination) {
        // For non-Redis servers, cross-server teleport is not supported
    }

    @Override
    public void crossServerTeleportToPlayer(Player player, TeleportType teleportType, String targetPlayerName, String targetServer) {
        // For non-Redis servers, cross-server teleport is not supported
    }

    @Override
    public String getServerName() {
        return this.plugin.getConfiguration().getServerName();
    }

    @Override
    public String findPlayerServer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null ? getServerName() : null;
    }
}
