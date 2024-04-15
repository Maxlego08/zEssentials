package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandKick extends VCommand {
    public CommandKick(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_KICK);
        this.setDescription(Message.DESCRIPTION_KICK);
        this.addRequireArg("player");
        this.addOptionalArg("reason");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String reason = args.length > 1 ? getArgs(1) : plugin.getModuleManager().getModule(SanctionModule.class).getKickDefaultReason();

        player.kick(getComponentMessage(Message.COMMAND_KICK_RECEIVER, "%reason%", reason));
        broadcast(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", player.getName(), "%reason%", reason);

        return CommandResultType.SUCCESS;
    }
}
