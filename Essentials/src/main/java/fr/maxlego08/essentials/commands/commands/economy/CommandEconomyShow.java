package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEconomyShow extends VCommand {


    public CommandEconomyShow(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ECO_SHOW);
        this.setDescription(Message.DESCRIPTION_ECO_SHOW);
        this.addSubCommand("show", "s");
        this.addRequireArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String userName = this.argAsString(0);
        EconomyProvider economyProvider = plugin.getEconomyProvider();

        plugin.getStorageManager().getStorage().getUserEconomy(userName, economies -> {

            if (economies.isEmpty()) {
                message(sender, Message.COMMAND_ECONOMY_SHOW_EMPTY, "%player%", userName);
                return;
            }

            economies.forEach(economyDTO -> {
                economyProvider.getEconomy(economyDTO.economy_name()).ifPresentOrElse(economy -> {
                    String economyFormat = economy.format(economyProvider.format(economyDTO.amount()), economyDTO.amount().longValue());
                    message(sender, Message.COMMAND_ECONOMY_SHOW_INFO, "%economy%", economy.getDisplayName(), "%amount%", economyFormat);
                }, () -> {
                    message(sender, Message.COMMAND_ECONOMY_SHOW_INFO, "%economy%", economyDTO.economy_name(), "%amount%", economyProvider.format(economyDTO.amount()));
                });
            });
        });

        return CommandResultType.SUCCESS;
    }
}
