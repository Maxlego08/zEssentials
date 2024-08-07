package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.time.Duration;
import java.util.ArrayList;

public class CommandBan extends VCommand {
    public CommandBan(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_BAN);
        this.setDescription(Message.DESCRIPTION_BAN);
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("duration", (a, b) -> this.cooldowns);
        this.addOptionalArg("reason", (a, b) -> new ArrayList<>());
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        SanctionModule sanctionModule = plugin.getModuleManager().getModule(SanctionModule.class);
        String userName = this.argAsString(0);
        Duration duration = this.argAsDuration(1);
        String reason = args.length > 2 ? getArgs(2) : sanctionModule.getBanDefaultReason();

        fetchUniqueId(userName, uuid -> sanctionModule.ban(sender, uuid, userName, duration, reason));

        return CommandResultType.SUCCESS;
    }
}
