package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandMoney extends VCommand {
    public CommandMoney(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_MONEY);
        this.setDescription(Message.DESCRIPTION_MONEY);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        List<String> arguments = new ArrayList<>();
        EconomyProvider economyProvider = plugin.getEconomyProvider();
        for (Map.Entry<String, BigDecimal> entry : this.user.getBalances().entrySet()) {

            economyProvider.getEconomy(entry.getKey()).ifPresent(economy -> {
                arguments.add("%economy-name-" + entry.getKey() + "%");
                arguments.add(economy.getDisplayName());
                arguments.add("%economy-" + entry.getKey() + "%");
                arguments.add(economy.format(economyProvider.format(entry.getValue()), entry.getValue().longValue()));
            });
        }

        message(sender, Message.COMMAND_MONEY, arguments.toArray());

        return CommandResultType.SUCCESS;
    }
}
