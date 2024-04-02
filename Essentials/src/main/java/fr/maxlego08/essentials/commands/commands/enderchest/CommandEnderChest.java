package fr.maxlego08.essentials.commands.commands.enderchest;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandEnderChest extends VCommand {

    public CommandEnderChest(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_ENDERCHEST);
        this.setDescription(Message.DESCRIPTION_DAY);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        this.player.openInventory(this.player.getEnderChest());
        return CommandResultType.SUCCESS;
    }
}
