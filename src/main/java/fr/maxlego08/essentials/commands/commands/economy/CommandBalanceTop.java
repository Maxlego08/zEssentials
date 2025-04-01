package fr.maxlego08.essentials.commands.commands.economy;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.economy.EconomyModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandBalanceTop extends VCommand {


    public CommandBalanceTop(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(EconomyModule.class);
        this.setPermission(Permission.ESSENTIALS_BALANCE_TOP);
        this.setDescription(Message.DESCRIPTION_BALANCE_TOP);
        this.addOptionalArg("page");
        this.onlyPlayers();
        this.addSubCommand(new CommandBalanceTopRefresh(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        int page = this.argAsInteger(0, 0);
        plugin.getEconomyManager().sendBaltop(player, page);

        return CommandResultType.SUCCESS;
    }
}
