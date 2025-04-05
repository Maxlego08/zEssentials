package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.ItemModule;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandMailClear extends VCommand {

    public CommandMailClear(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_CLEAR);
        this.setPermission(Permission.ESSENTIALS_MAIL_CLEAR);
        this.addSubCommand("clear");
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String username = this.argAsString(0);
        var module = plugin.getModuleManager().getModule(MailBoxModule.class);
        this.fetchUniqueId(username, uuid -> module.clear(sender, uuid, username));

        return CommandResultType.SUCCESS;
    }
}
