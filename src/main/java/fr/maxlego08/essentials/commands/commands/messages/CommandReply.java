package fr.maxlego08.essentials.commands.commands.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.ArrayList;

public class CommandReply extends VCommand {
    public CommandReply(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_REPLY);
        this.setDescription(Message.DESCRIPTION_REPLY);
        this.addRequireArg("message", (a, b) -> new ArrayList<>());
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        MessageModule messageModule = plugin.getModuleManager().getModule(MessageModule.class);
        String message = getArgs(0);

        if (!user.hasPrivateMessage()) {
            return CommandResultType.DEFAULT;
        }

        PrivateMessage privateMessage = user.getPrivateMessage();
        isOnline(privateMessage.username(), () -> messageModule.sendMessage(this.user, privateMessage.uuid(), privateMessage.username(), message));

        return CommandResultType.SUCCESS;
    }
}
