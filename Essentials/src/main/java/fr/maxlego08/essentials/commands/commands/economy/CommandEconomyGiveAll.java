package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

public class CommandEconomyGiveAll extends VCommand {


    public CommandEconomyGiveAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_ECO_GIVE_ALL);
        this.setDescription(Message.DESCRIPTION_ECO_GIVE_ALL);
        this.addSubCommand("giveall");
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyManager().getEconomies().stream().map(Economy::getName).toList());
        this.addRequireArg("amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addBooleanOptionalArg("silent");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        double amount = this.argAsDouble(1);
        boolean silent = this.argAsBoolean(2, false);

        EconomyManager economyManager = plugin.getEconomyManager();
        Optional<Economy> optional = economyManager.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }
        Economy economy = optional.get();
        String economyFormat = economyManager.format(economy, amount);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            economyManager.deposit(player.getUniqueId(), economy, new BigDecimal(amount));
            if (!silent) {
                message(onlinePlayer, Message.COMMAND_ECONOMY_GIVE_RECEIVER, "%economyFormat%", economyFormat);
            }
        });

        message(sender, Message.COMMAND_ECONOMY_GIVE_ALL_SENDER, "%economyFormat%", economyFormat);

        return CommandResultType.SUCCESS;
    }
}
