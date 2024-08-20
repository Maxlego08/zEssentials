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
        this.addOptionalOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        EconomyManager economyManager = plugin.getEconomyManager();
        String userName = this.argAsString(0, null);
        if (userName == null || userName.equalsIgnoreCase(user.getName()) && hasPermission(sender, Permission.ESSENTIALS_MONEY_OTHER)) {

            List<String> arguments = new ArrayList<>();
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

        plugin.getStorageManager().getStorage().getUserEconomy(userName, economies -> {

            List<String> arguments = new ArrayList<>();
            arguments.add("%player%");
            arguments.add(userName);

            economies.forEach(economyDTO -> {

                BigDecimal amount = economyDTO.amount();

                economyManager.getEconomy(economyDTO.economy_name()).ifPresentOrElse(economy -> {

                    arguments.add("%economy-name-" + economy.getName() + "%");
                    arguments.add(economy.getDisplayName());
                    arguments.add("%economy-" + economy.getName() + "%");
                    arguments.add(economyManager.format(economy, amount));
                }, () -> {

                    arguments.add("%economy-name-" + economyDTO.economy_name() + "%");
                    arguments.add(economyDTO.economy_name());
                    arguments.add("%economy-" + economyDTO.economy_name() + "%");
                    arguments.add(economyManager.format(amount));
                });
            });
            message(sender, Message.COMMAND_MONEY_OTHER, arguments.toArray());

        });

        return CommandResultType.SUCCESS;
    }
}
