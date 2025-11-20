package fr.maxlego08.essentials.commands.commands.trade;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.trade.TradeManager;
import fr.maxlego08.essentials.module.modules.trade.TradeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class CommandTradeAccept extends VCommand {

    public CommandTradeAccept(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("accept");
        this.setPermission(Permission.ESSENTIALS_TRADE_ACCEPT);
        this.setDescription(Message.DESCRIPTION_TRADE);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        TradeModule module = plugin.getModuleManager().getModule(TradeModule.class);
        TradeManager manager = module.getTradeManager();
        
        if (args.length == 0) {
            Map<UUID, UUID> requests = manager.getRequests();
            UUID senderUUID = requests.get(player.getUniqueId());
            if (senderUUID != null) {
                Player senderPlayer = Bukkit.getPlayer(senderUUID);
                if (senderPlayer != null) {
                    manager.acceptRequest(senderPlayer, player);
                    return CommandResultType.SUCCESS;
                }
            }
            message(sender, Message.COMMAND_NO_ARG);
            return CommandResultType.SYNTAX_ERROR;
        }
        
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
             message(sender, Message.PLAYER_NOT_FOUND, "%player%", targetName);
             return CommandResultType.DEFAULT;
        }
        
        manager.acceptRequest(target, player);
        return CommandResultType.SUCCESS;
    }
}

