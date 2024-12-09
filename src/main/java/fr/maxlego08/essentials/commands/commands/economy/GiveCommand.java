package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.Optional;

public abstract class GiveCommand extends VCommand {
    public GiveCommand(EssentialsPlugin plugin) {
        super(plugin);
    }

    protected CommandResultType give(CommandSender sender, String userName, String economyName, double amount, boolean silent) {
        EconomyManager economyManager = plugin.getEconomyManager();
        Optional<Economy> optional = economyManager.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }

        Economy economy = optional.get();
        fetchUniqueId(userName, uniqueId -> {

            economyManager.deposit(uniqueId, economy, new BigDecimal(amount));

            String economyFormat = economyManager.format(economy, amount);
            message(sender, Message.COMMAND_ECONOMY_GIVE_SENDER, "%player%", userName, "%economyFormat%", economyFormat);
            if (!silent) {
                message(uniqueId, Message.COMMAND_ECONOMY_GIVE_RECEIVER, "%economyFormat%", economyFormat);
            }
        });
        return CommandResultType.SUCCESS;
    }

}
