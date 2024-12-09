package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.economy.EconomyModule;

import java.util.stream.Stream;

public class CommandEconomyGiveRandom extends GiveCommand {


    public CommandEconomyGiveRandom(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_ECO_GIVE_RANDOM);
        this.setDescription(Message.DESCRIPTION_ECO_GIVE_RANDOM);
        this.addSubCommand("give-random", "gr");
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyManager().getEconomies().stream().map(Economy::getName).toList());
        this.addRequireOfflinePlayerNameArg();
        this.addRequireArg("min-amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addRequireArg("max-amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addBooleanOptionalArg("silent");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        String userName = this.argAsString(1);
        double minAmount = this.argAsDouble(2);
        double maxAmount = this.argAsDouble(3);
        boolean silent = this.argAsBoolean(4, false);

        minAmount = Math.min(minAmount, maxAmount);
        maxAmount = Math.max(minAmount, maxAmount);

        return give(this.sender, userName, economyName, minAmount + Math.random() * (maxAmount - minAmount), silent);
    }
}
