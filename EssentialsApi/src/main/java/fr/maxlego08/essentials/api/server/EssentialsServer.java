package fr.maxlego08.essentials.api.server;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public interface EssentialsServer {

    void onEnable();

    void onDisable();

    List<String> getPlayersNames();

    void sendMessage(UUID uuid, Message message, Object... objects);

    void broadcastMessage(Permission permission, Message message, Object... objects);

    void kickPlayer(UUID uuid, Message message, Object... objects);

    boolean isOnline(String userName);

    void clearChat(CommandSender sender);

    void toggleChat(boolean value);
}
