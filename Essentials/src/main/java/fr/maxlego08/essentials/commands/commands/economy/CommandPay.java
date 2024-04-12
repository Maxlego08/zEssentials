package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.economy.NumberMultiplicationFormat;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

public class CommandPay extends VCommand {

    public CommandPay(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_PAY);
        this.setDescription(Message.DESCRIPTION_PAY);
        this.onlyPlayers();
        this.addRequireArg("player");
        this.addRequireArg("amount", (a, b) -> Stream.of(10, 20, 30, 40, 50, 60, 70, 80, 90).map(String::valueOf).toList());
        this.addOptionalArg("economy", (a, b) -> plugin.getEconomyProvider().getEconomies().stream().map(Economy::getName).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        EconomyProvider economyProvider = plugin.getEconomyProvider();
        String userName = this.argAsString(0);
        String amountAsString = this.argAsString(1);
        String economyName = this.argAsString(2, economyProvider.getDefaultEconomy().getName());

        if (amountAsString.contains("-")) {
            message(sender, Message.COMMAND_PAY_NEGATIVE);
            return CommandResultType.DEFAULT;
        }

        final String sanitizedString = amountAsString.replaceAll("[^0-9.]", "");
        if (sanitizedString.isEmpty()) return CommandResultType.SYNTAX_ERROR;

        BigDecimal amount = new BigDecimal(amountAsString.replaceAll("[^0-9.]", ""));
        String format = amountAsString.replace(sanitizedString, "");
        Optional<NumberMultiplicationFormat> optional = economyProvider.getMultiplication(format);
        if (optional.isPresent()) {
            NumberMultiplicationFormat numberMultiplicationFormat = optional.get();
            amount = amount.multiply(numberMultiplicationFormat.multiplication());
        }

        Optional<Economy> optionalEconomy = economyProvider.getEconomy(economyName);
        if (optionalEconomy.isEmpty()) {
            message(sender, Message.COMMAND_ECONOMY_NOT_FOUND, "%name%", economyName);
            return CommandResultType.DEFAULT;
        }

        Economy economy = optionalEconomy.get();
        if (!economy.isEnablePay()) {
            message(sender, Message.COMMAND_PAY_DISABLE, "%name%", economy.getDisplayName());
            return CommandResultType.DEFAULT;
        }

        if (amount.compareTo(economy.getMinPayValue()) < 0) {
            message(sender, Message.COMMAND_PAY_MIN, "%amount%", economy.format(economyProvider.format(economy.getMinPayValue()), economy.getMinPayValue().longValue()));
            return CommandResultType.DEFAULT;
        }

        if (amount.compareTo(economy.getMaxPayValue()) > 0) {
            message(sender, Message.COMMAND_PAY_MAX, "%amount%", economy.format(economyProvider.format(economy.getMaxPayValue()), economy.getMaxPayValue().longValue()));
            return CommandResultType.DEFAULT;
        }

        if (userName.equalsIgnoreCase(player.getName())) {
            message(sender, Message.COMMAND_PAY_SELF);
            return CommandResultType.DEFAULT;
        }

        if (user.getBalance(economy).compareTo(amount) < 0) {
            message(sender, Message.COMMAND_PAY_NOT_ENOUGH);
            return CommandResultType.DEFAULT;
        }

        BigDecimal finalAmount = amount;
        this.fetchUniqueId(userName, uniqueId -> {

            if (economy.isEnableConfirmInventory() && finalAmount.compareTo(economy.getMinConfirmInventory()) > 0) {
                User fakeUser = new ZUser(plugin, uniqueId);
                fakeUser.setName(userName);
                this.user.setTargetPay(fakeUser, economy, finalAmount);
                plugin.getScheduler().runAtLocation(player.getLocation(), wrappedTask -> plugin.getInventoryManager().openInventory(player, plugin, "confirm_pay_inventory"));
                return;
            }

            economyProvider.pay(this.player.getUniqueId(), this.player.getName(), uniqueId, userName, economy, finalAmount);
        });

        return CommandResultType.SUCCESS;
    }
}
