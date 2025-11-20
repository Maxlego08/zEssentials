package fr.maxlego08.essentials.commands.commands.trade;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.trade.TradeModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandTrade extends VCommand {

    public CommandTrade(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TradeModule.class);
        this.setPermission(Permission.ESSENTIALS_TRADE_USE);
        this.setDescription(Message.DESCRIPTION_TRADE);
        
        this.addSubCommand(new CommandTradeAccept(plugin));
        this.addSubCommand(new CommandTradeDeny(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        if (args.length == 0) {
            syntaxMessage();
            return CommandResultType.SYNTAX_ERROR;
        }
        
        String targetName = args[0];
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null) {
            message(sender, Message.PLAYER_NOT_FOUND, "%player%", targetName);
            return CommandResultType.DEFAULT;
        }
        
        TradeModule module = plugin.getModuleManager().getModule(TradeModule.class);
        module.getTradeManager().sendRequest(player, target);
        
        return CommandResultType.SUCCESS;
    }
}

