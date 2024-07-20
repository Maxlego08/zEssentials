package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

public class CommandSeen extends VCommand {
    public CommandSeen(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_SEEN);
        this.setDescription(Message.DESCRIPTION_SEEN);
        this.addRequirePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        SanctionModule sanctionModule = plugin.getModuleManager().getModule(SanctionModule.class);
        String userName = this.argAsString(0);

        CommandSender commandSender = this.sender;
        fetchUniqueId(userName, uuid -> sanctionModule.seen(commandSender, uuid));

        return CommandResultType.SUCCESS;
    }
}
