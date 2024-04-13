package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandLoom extends VCommand {
    public CommandLoom(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_LOOM);
        this.setDescription(Message.DESCRIPTION_LOOM);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.getPlayer().openLoom(this.player.getLocation(), true);

        return CommandResultType.SUCCESS;
    }
}
