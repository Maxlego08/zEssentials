package fr.maxlego08.essentials.commands.commands.chat;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ChatModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;

public class CommandChatClear extends VCommand {

    public CommandChatClear(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ChatModule.class);
        this.setPermission(Permission.ESSENTIALS_CHAT_CLEAR);
        this.setDescription(Message.DESCRIPTION_CHAT_CLEAR);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        plugin.getEssentialsServer().clearChat(sender);
        return CommandResultType.SUCCESS;
    }
}
