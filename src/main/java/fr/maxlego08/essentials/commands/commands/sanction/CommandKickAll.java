package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;

public class CommandKickAll extends VCommand {
    public CommandKickAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SanctionModule.class);
        this.setPermission(Permission.ESSENTIALS_KICK_ALL);
        this.setDescription(Message.DESCRIPTION_KICK_ALL);
        this.addOptionalArg("reason");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String reason = args.length > 0 ? getArgs(0) : plugin.getModuleManager().getModule(SanctionModule.class).getKickDefaultReason();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!hasPermission(player, Permission.ESSENTIALS_KICK_BYPASS_ALL)) {
                plugin.getEssentialsServer().kickPlayer(player.getUniqueId(), Message.MESSAGE_KICK, "%reason%", reason);
            }
        });
        broadcast(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", player.getName(), "%reason%", reason);

        return CommandResultType.SUCCESS;
    }
}
