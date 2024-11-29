package fr.maxlego08.essentials.commands.commands.clearinventory;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class ClearInventoryCommand extends VCommand {
    public ClearInventoryCommand(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_CLEARINVENTORY);
        this.setDescription(Message.DESCRIPTION_CLEARINVENTORY);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (!hasPermission(sender, Permission.ESSENTIALS_CLEARINVENTORY_OTHER)) {
            player = this.player;
        }

        player.getInventory().clear();
        message(sender, sender == player ? Message.COMMAND_CLEARINVENTORY_SUCCESS : Message.COMMAND_CLEARINVENTORY_SUCCESS_OTHER, player);

        return CommandResultType.SUCCESS;
    }
}