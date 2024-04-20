package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandChatBroadcast extends VCommand {

    public CommandChatBroadcast(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ChatModule.class);
        this.setPermission(Permission.ESSENTIALS_CHAT_BROADCAST);
        this.setDescription(Message.DESCRIPTION_CHAT_BROADCAST);
        this.addRequireArg("message");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        String message = getArgs(0);
        plugin.getEssentialsServer().broadcast(message);
        return CommandResultType.SUCCESS;
    }
}
