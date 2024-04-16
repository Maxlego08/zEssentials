package fr.maxlego08.essentials.api.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessage;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public interface EssentialsUtils {

    void message(UUID uniqueId, Message message, Object... args);

    void message(User sender, Message message, Object... args);

    void message(CommandSender sender, Message message, Object... args);

    void broadcast(Permission permission, Message message, Object... args);

    void process(ServerMessage receivedMessage);
}
