package fr.maxlego08.essentials.api.server;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import org.bukkit.entity.Player;

import java.util.List;

public interface EssentialsServer {

    void onEnable();

    void onDisable();

    List<String> getPlayersNames();

    void sendMessage(Player player, Message message, Object... objects);

    void broadcastMessage(Permission permission, Message message, Object... objects);

}
