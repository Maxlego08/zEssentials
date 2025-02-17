package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandPub extends VCommand {

    public CommandPub(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_PUB);
        this.setDescription(Message.DESCRIPTION_PUB);
        this.addRequireArg("message");
        this.onlyPlayers();
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String message = getArgs(0);
        var module = plugin.getModuleManager().getModule(ChatModule.class);

        if (module.getPubPattern().matcher(message).find()) {
            message(player, Message.COMMAND_PUB_PATTERN_ERROR);
            return CommandResultType.DEFAULT;
        }

        plugin.getEssentialsServer().pub(player, message);

        return CommandResultType.SUCCESS;
    }
}
