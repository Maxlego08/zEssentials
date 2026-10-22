package fr.maxlego08.essentials.commands.commands.sanction;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SanctionModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        SanctionModule sanctionModule = plugin.getModuleManager().getModule(SanctionModule.class);
        String reason = args.length > 0 ? getArgs(0) : sanctionModule.getKickDefaultReason();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!hasPermission(player, Permission.ESSENTIALS_KICK_BYPASS_ALL)) {
                plugin.getEssentialsServer().kickPlayer(player.getUniqueId(), Message.MESSAGE_KICK, "%reason%", reason);
            }
        });
        broadcast(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", "all", "%reason%", reason,
                "%sender%", sanctionModule.getSanctionBy(sender), "%duration%", "0", "%created_at%", new SimpleDateFormat(sanctionModule.getDateFormat()).format(new Date()));

        return CommandResultType.SUCCESS;
    }
}
