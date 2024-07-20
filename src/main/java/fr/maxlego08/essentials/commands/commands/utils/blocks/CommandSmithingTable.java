package fr.maxlego08.essentials.commands.commands.utils.blocks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandSmithingTable extends VCommand {
    public CommandSmithingTable(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_SMITHINGTABLE);
        this.setDescription(Message.DESCRIPTION_SMITHINGTABLE);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        this.player.getPlayer().openSmithingTable(this.player.getLocation(), true);

        return CommandResultType.SUCCESS;
    }
}
