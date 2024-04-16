package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.exception.PlayerNotFoundError;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandKick extends VCommand {
    public CommandKick(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_KICK);
        this.setDescription(Message.DESCRIPTION_KICK);
        this.addRequirePlayerNameArg();
        this.addOptionalArg("reason");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) throws Exception {

        SanctionModule sanctionModule = plugin.getModuleManager().getModule(SanctionModule.class);
        String playerName = this.argAsOnlinePlayerName(0);
        String reason = args.length > 1 ? getArgs(1) : sanctionModule.getKickDefaultReason();

        sanctionModule.kick(sender, playerName, reason);

        return CommandResultType.SUCCESS;
    }
}
