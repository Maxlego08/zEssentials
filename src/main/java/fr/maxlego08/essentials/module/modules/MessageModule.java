package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.chat.ChatResult;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;

import java.util.Map;
import java.util.UUID;

public class MessageModule extends ZModule {

    public MessageModule(ZEssentialsPlugin plugin) {
        super(plugin, "messages");
    }

    public void sendMessage(User user, UUID uuid, String userName, String message) {

        EssentialsServer essentialsServer = this.plugin.getEssentialsServer();
        IStorage iStorage = this.plugin.getStorageManager().getStorage();

        if (user.getUniqueId().equals(uuid)) {
            message(user, Message.COMMAND_MESSAGE_SELF);
            return;
        }

        Map<Option, Boolean> options = iStorage.getOptions(uuid);
        if (options.getOrDefault(Option.VANISH, false)) {
            message(user, Message.PLAYER_NOT_FOUND, "%player%", userName);
            return;
        }

        if (options.getOrDefault(Option.PRIVATE_MESSAGE_DISABLE, false)) {
            message(user, Message.COMMAND_MESSAGE_DISABLE, "%player%", userName);
            return;
        }

        if (user.isMute()) {
            message(user, Message.COMMAND_MESSAGE_MUTE);
            return;
        }

        ChatModule chatModule = plugin.getModuleManager().getModule(ChatModule.class);
        ChatResult chatResult = chatModule.analyzeMessage(user, message);
        if (!chatResult.isValid()) {
            message(user, chatResult.message(), chatResult.arguments());
            return;
        }

        PrivateMessage privateMessage = user.setPrivateMessage(uuid, userName);
        this.plugin.getUtils().sendPrivateMessage(user, privateMessage, Message.COMMAND_MESSAGE_ME, message);
        essentialsServer.sendPrivateMessage(user, privateMessage, message);
        essentialsServer.broadcastMessage(Option.SOCIAL_SPY, Message.COMMAND_MESSAGE_SOCIAL_SPY, "%sender%", user.getName(), "%receiver%", userName, "%message%", message);
    }
}
