package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class CommandEconomyGive extends VCommand {


    public CommandEconomyGive(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ECO_GIVE);
        this.setDescription(Message.DESCRIPTION_ECO_GIVE);
        this.addSubCommand("give");
        this.addRequireArg("economy", (a, b) -> plugin.getEconomyProvider().getEconomies().stream().map(Economy::getName).toList());
        this.addRequireArg("player");
        this.addRequireArg("amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String economyName = this.argAsString(0);
        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(1);
        double amount = this.argAsDouble(2);
        boolean silent = this.argAsBoolean(3, false);

        EconomyProvider economyProvider = plugin.getEconomyProvider();
        Optional<Economy> optional = economyProvider.getEconomy(economyName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }
        Economy economy = optional.get();
        economyProvider.deposit(player, economy, new BigDecimal(amount));

        String economyFormat = economy.format(economyProvider.format(amount), (long) amount);
        message(sender, Message.COMMAND_ECONOMY_GIVE_SENDER, "%player%", offlinePlayer.getName(), "%economyFormat%", economyFormat);
        if (offlinePlayer.isOnline() && !silent) {
            message(offlinePlayer.getPlayer(), Message.COMMAND_ECONOMY_GIVE_RECEIVER, "%economyFormat%", economyFormat);
        }

        return CommandResultType.SUCCESS;
    }
}
