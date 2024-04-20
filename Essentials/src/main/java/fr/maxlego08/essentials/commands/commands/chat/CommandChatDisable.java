package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandChatDisable extends VCommand {
    public CommandChatDisable(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ChatModule.class);
        this.setPermission(Permission.ESSENTIALS_CHAT_DISABLE);
        this.setDescription(Message.DESCRIPTION_CHAT_DISABLE);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        if (!ConfigStorage.chatDisable) {
            message(sender, Message.COMMAND_CHAT_DISABLE_ERROR);
            return CommandResultType.DEFAULT;
        }

        plugin.getEssentialsServer().toggleChat(false);
        message(sender, Message.COMMAND_CHAT_DISABLE_SUCCESS);
        return CommandResultType.SUCCESS;
    }
}
