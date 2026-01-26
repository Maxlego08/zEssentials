package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandExt extends VCommand {
    public CommandExt(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXT);
        this.setDescription(Message.DESCRIPTION_EXT);
        this.addOptionalArg("player", getVisiblePlayerNames());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) return CommandResultType.SYNTAX_ERROR;

        if (player != this.player && !hasPermission(sender, Permission.ESSENTIALS_FEED_OTHER)) {
            player = this.player;
        }

        if (!player.isValid()) {
            message(sender, Message.COMMAND_EXT_ERROR);
            return CommandResultType.DEFAULT;
        }

        this.player.setFireTicks(0);

        if (player == sender) {

            message(sender, Message.COMMAND_EXT_RECEIVER);
        } else {

            message(sender, Message.COMMAND_EXT_SENDER, "%player%", player.getName());
            message(player, Message.COMMAND_EXT_RECEIVER);
        }

        return CommandResultType.SUCCESS;
    }
}
