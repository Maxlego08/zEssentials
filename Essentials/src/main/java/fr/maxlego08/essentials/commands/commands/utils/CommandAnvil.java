package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandAnvil extends VCommand {
    public CommandAnvil(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ANVIL);
        this.setDescription(Message.DESCRIPTION_ANVIL);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.getPlayer().openAnvil(this.player.getLocation(), true);

        return CommandResultType.SUCCESS;
    }
}
