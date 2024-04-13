package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandStoneCutter extends VCommand {
    public CommandStoneCutter(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_STONECUTTER);
        this.setDescription(Message.DESCRIPTION_STONECUTTER);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.getPlayer().openStonecutter(this.player.getLocation(), true);

        return CommandResultType.SUCCESS;
    }
}
