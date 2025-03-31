package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEconomyShow extends VCommand {


    public CommandEconomyShow(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_ECO_SHOW);
        this.setDescription(Message.DESCRIPTION_ECO_SHOW);
        this.addSubCommand("show", "s");
        this.addRequireOfflinePlayerNameArg();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String userName = this.argAsString(0);
        EconomyManager economyManager = plugin.getEconomyManager();

        plugin.getStorageManager().getStorage().getUserEconomy(userName, economies -> {

            if (economies.isEmpty()) {
                message(sender, Message.COMMAND_ECONOMY_SHOW_EMPTY, "%player%", userName);
                return;
            }

            economies.forEach(economyDTO -> {
                economyManager.getEconomy(economyDTO.economy_name()).ifPresentOrElse(economy -> {
                    String economyFormat = economyManager.format(economy,  economyDTO.amount());
                    message(sender, Message.COMMAND_ECONOMY_SHOW_INFO, "%economy%", economy.getDisplayName(), "%amount%", economyFormat);
                }, () -> {
                    message(sender, Message.COMMAND_ECONOMY_SHOW_INFO, "%economy%", economyDTO.economy_name(), "%amount%", economyManager.format(economyDTO.amount()));
                });
            });
        });

        return CommandResultType.SUCCESS;
    }
}
