package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandChatEnable extends VCommand {
    public CommandChatEnable(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ChatModule.class);
        this.setPermission(Permission.ESSENTIALS_CHAT_ENABLE);
        this.setDescription(Message.DESCRIPTION_CHAT_ENABLE);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (ConfigStorage.chatEnable) {
            message(sender, Message.COMMAND_CHAT_ENABLE_ERROR);
            return CommandResultType.DEFAULT;
        }

        plugin.getEssentialsServer().toggleChat(true);
        message(sender, Message.COMMAND_CHAT_ENABLE_SUCCESS);
        return CommandResultType.SUCCESS;
    }
}
