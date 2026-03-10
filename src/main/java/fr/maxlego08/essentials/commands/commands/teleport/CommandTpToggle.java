package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.MessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to toggle receiving teleport requests.
 * Allows players to enable/disable receiving TPA and TPAHERE requests from other players.
 */
public class CommandTpToggle extends VCommand {
    
    public CommandTpToggle(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_TP_TOGGLE);
        this.setDescription(Message.DESCRIPTION_TP_TOGGLE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        // Check if toggling for self or if user has permission to toggle for others
        if (player == this.player || !hasPermission(sender, Permission.ESSENTIALS_TP_TOGGLE_OTHER)) {
            toggleTeleportRequest(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            toggleTeleportRequest(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    /**
     * Toggle the teleport request option for the specified user.
     * 
     * @param player The player whose setting is being toggled
     * @param user The user object to modify
     * @param sender The command sender who executed the command
     */
    private void toggleTeleportRequest(Player player, User user, CommandSender sender) {

        user.setOption(Option.TELEPORT_REQUEST_DISABLE, !user.getOption(Option.TELEPORT_REQUEST_DISABLE));
        boolean isTeleportRequestDisable = user.getOption(Option.TELEPORT_REQUEST_DISABLE);

        Message messageKey = isTeleportRequestDisable ? Message.COMMAND_TP_TOGGLE_DISABLE : Message.COMMAND_TP_TOGGLE_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}
