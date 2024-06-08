package fr.maxlego08.essentials.commands.commands.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandMailOpen extends VCommand {

    public CommandMailOpen(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MailBoxModule.class);
        this.setDescription(Message.DESCRIPTION_MAIL_OPEN);
        this.setPermission(Permission.ESSENTIALS_MAIL_OPEN);
        this.addSubCommand("open");
        this.addRequirePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String username = this.argAsString(0);
        User user = this.user;
        this.fetchUniqueId(username, uuid -> plugin.getModuleManager().getModule(MailBoxModule.class).openMailBox(user, uuid, username));

        return CommandResultType.SUCCESS;
    }
}
