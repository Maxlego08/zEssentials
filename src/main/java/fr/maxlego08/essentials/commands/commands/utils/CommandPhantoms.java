package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command that allows players to toggle phantom spawning for themselves or others.
 */
public class CommandPhantoms extends VCommand {

    public CommandPhantoms(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_PHANTOMS);
        this.setDescription(Message.DESCRIPTION_PHANTOMS);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player || !hasPermission(sender, Permission.ESSENTIALS_PHANTOMS_OTHER)) {
            togglePhantoms(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            togglePhantoms(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void togglePhantoms(Player player, User user, CommandSender sender) {

        user.setOption(Option.PHANTOMS_DISABLE, !user.getOption(Option.PHANTOMS_DISABLE));
        boolean disabled = user.getOption(Option.PHANTOMS_DISABLE);

        Message messageKey = disabled ? Message.COMMAND_PHANTOMS_DISABLE : Message.COMMAND_PHANTOMS_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}

