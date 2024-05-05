package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.ServerMessageType;
import fr.maxlego08.essentials.api.server.messages.ServerMessage;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.EssentialsUtils;
import fr.maxlego08.essentials.storage.ConfigStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.UUID;

public abstract class BaseServer extends ZUtils implements EssentialsUtils {

    protected final EssentialsPlugin plugin;

    public BaseServer(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void message(UUID uniqueId, Message message, Object... args) {
        super.message(uniqueId, message, args);
    }

    @Override
    public void message(User sender, Message message, Object... args) {
        super.message(sender, message, args);
    }

    @Override
    public void message(CommandSender sender, Message message, Object... args) {
        super.message(sender, message, args);
    }

    @Override
    public void broadcast(Permission permission, Message message, Object... args) {
        super.broadcast(permission, message, args);
    }

    @Override
    public void broadcast(Message message, Object... arguments) {
        super.broadcast(message, arguments);
    }

    @Override
    public void process(ServerMessage receivedMessage) {
        if (receivedMessage.serverMessageType() == ServerMessageType.BROADCAST_PERMISSION) {
            this.broadcast(receivedMessage.permission(), receivedMessage.message(), receivedMessage.arguments());
        } else if (receivedMessage.serverMessageType() == ServerMessageType.BROADCAST) {
            this.broadcast(receivedMessage.message(), receivedMessage.arguments());
        } else {
            this.message(receivedMessage.uuid(), receivedMessage.message(), receivedMessage.arguments());
        }
    }

    @Override
    public Object createInstanceFromMap(Constructor<?> constructor, Map<?, ?> map) {
        return super.createInstanceFromMap(constructor, map);
    }

    @Override
    public void toggleChat(boolean value) {
        ConfigStorage.chatEnable = value;
        ConfigStorage.getInstance().save(this.plugin.getPersist());
    }


    @Override
    public String getMessage(Message message, Object... objects) {
        return super.getMessage(message, objects);
    }

    @Override
    public void broadcast(Option option, Message message, Object... objects) {
        IStorage iStorage = plugin.getStorageManager().getStorage();
        Bukkit.getOnlinePlayers().forEach(player -> {
            User user = iStorage.getUser(player.getUniqueId());
            if (user != null && user.getOption(option)) {
                message(user, message, objects);
            }
        });
    }

    @Override
    public void deleteCooldown(UUID uniqueId, String cooldownName) {
        User user = this.plugin.getUser(uniqueId);
        if (user == null) return;
        user.removeCooldown(cooldownName);
    }
}
