package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandChatHistory extends VCommand {

    public CommandChatHistory(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_CHAT_HISTORY);
        this.setDescription(Message.DESCRIPTION_CHAT_HISTORY);
        this.addRequirePlayerNameArg();
        this.addOptionalArg("page");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        String username = this.argAsString(0);
        int page = this.argAsInteger(1, 1);

        fetchUniqueId(username, uuid -> plugin.getModuleManager().getModule(ChatModule.class).sendChatHistory(sender, uuid, username, page));

        return CommandResultType.SUCCESS;
    }
}
