package fr.maxlego08.essentials.commands.commands.messages;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.ArrayList;

public class CommandMessage extends VCommand {
    public CommandMessage(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_MESSAGE);
        this.setDescription(Message.DESCRIPTION_MESSAGE);
        this.addRequirePlayerNameArg();
        this.addRequireArg("message", (a, b) -> new ArrayList<>());
        this.setExtendedArgs(true);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        MessageModule messageModule = plugin.getModuleManager().getModule(MessageModule.class);
        String userName = this.argAsString(0);
        String message = getArgs(1);

        isOnline(userName, () -> fetchUniqueId(userName, uuid -> messageModule.sendMessage(this.user, uuid, userName, message)));

        return CommandResultType.SUCCESS;
    }
}
