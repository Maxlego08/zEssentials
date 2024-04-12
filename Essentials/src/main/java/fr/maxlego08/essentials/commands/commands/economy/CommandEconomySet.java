package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.economy.EconomyManager;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class CommandEconomySet extends VCommand {


    public CommandEconomySet(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyManager.class);
        this.setPermission(Permission.ESSENTIALS_ECO_SET);
        this.setDescription(Message.DESCRIPTION_ECO_SET);
        this.addSubCommand("set");
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyProvider().getEconomies().stream().map(Economy::getName).toList());
        this.addRequireArg("player");
        this.addRequireArg("amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        String userName = this.argAsString(1);
        double amount = this.argAsDouble(2);
        boolean silent = this.argAsBoolean(3, false);

        EconomyProvider economyProvider = plugin.getEconomyProvider();
        Optional<Economy> optional = economyProvider.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }
        Economy economy = optional.get();

        fetchUniqueId(userName, uniqueId -> {
            economyProvider.set(uniqueId, economy, new BigDecimal(amount));

            String economyFormat = economyProvider.format(economy, amount);
            message(sender, Message.COMMAND_ECONOMY_SET_SENDER, "%player%", userName, "%economyFormat%", economyFormat);

            if (!silent) {
                message(uniqueId, Message.COMMAND_ECONOMY_SET_RECEIVER, "%economyFormat%", economyFormat);
            }
        });

        return CommandResultType.SUCCESS;
    }
}
