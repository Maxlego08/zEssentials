package fr.maxlego08.essentials.api.server;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public interface EssentialsServer {

    void onEnable();

    void onDisable();

    List<String> getPlayersNames();

    void sendMessage(UUID uuid, Message message, Object... objects);

    void broadcastMessage(Permission permission, Message message, Object... objects);

    void broadcastMessage(Option option, Message message, Object... objects);

    void kickPlayer(UUID uuid, Message message, Object... objects);

    boolean isOnline(String userName);

    void clearChat(CommandSender sender);

    void toggleChat(boolean value);

    void broadcast(String message);

    void sendPrivateMessage(User user, PrivateMessage privateMessage, String message);

    void deleteCooldown(UUID uniqueId, String cooldownName);

    void updateCooldown(UUID uniqueId, String cooldownName, long expiredAt);

    List<String> getOfflinePlayersNames();
}
