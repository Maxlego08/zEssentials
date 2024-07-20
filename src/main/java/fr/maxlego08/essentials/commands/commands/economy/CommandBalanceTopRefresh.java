package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Optional;

public class CommandBalanceTopRefresh extends VCommand {


    public CommandBalanceTopRefresh(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.addSubCommand("refresh");
        this.setPermission(Permission.ESSENTIALS_BALANCE_TOP_REFRESH);
        this.setDescription(Message.DESCRIPTION_BALANCE_TOP_REFRESH);
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyManager().getEconomies().stream().map(Economy::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);

        EconomyManager economyManager = plugin.getEconomyManager();
        Optional<Economy> optional = economyManager.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }

        Economy economy = optional.get();
        economyManager.refreshBaltop(economy);

        return CommandResultType.SUCCESS;
    }
}
