package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommandMoney extends VCommand {
    public CommandMoney(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_MONEY);
        this.setDescription(Message.DESCRIPTION_MONEY);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        List<String> arguments = new ArrayList<>();
        EconomyManager economyManager = plugin.getEconomyManager();
        economyManager.getEconomies().forEach(economy -> {
            BigDecimal amount = this.user.getBalance(economy);
            arguments.add("%economy-name-" + economy.getName() + "%");
            arguments.add(economy.getDisplayName());
            arguments.add("%economy-" + economy.getName() + "%");
            arguments.add(economyManager.format(economy, amount));
        });

        message(sender, Message.COMMAND_MONEY, arguments.toArray());

        return CommandResultType.SUCCESS;
    }
}
