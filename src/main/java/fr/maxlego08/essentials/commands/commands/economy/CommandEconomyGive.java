package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.economy.EconomyModule;

import java.util.stream.Stream;

public class CommandEconomyGive extends GiveCommand {


    public CommandEconomyGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_ECO_GIVE);
        this.setDescription(Message.DESCRIPTION_ECO_GIVE);
        this.addSubCommand("give");
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyManager().getEconomies().stream().map(Economy::getName).toList());
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addBooleanOptionalArg("silent");
        this.addOptionalArg("reason");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        String userName = this.argAsString(1);
        double amount = this.argAsDouble(2);
        boolean silent = this.argAsBoolean(3, false);
        String reason = this.getArgs(5, getMessage(plugin.getEconomyManager().getCommandGiveReason(), "%sender%", sender.getName()));

        return give(this.sender, userName, economyName, amount, silent, reason);
    }
}
