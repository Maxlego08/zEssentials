package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGod extends VCommand {
    public CommandGod(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_GOD);
        this.setDescription(Message.DESCRIPTION_GOD);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player) {
            toggleGodMode(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            toggleGodMode(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void toggleGodMode(Player player, User user, CommandSender sender) {

        user.setOption(Option.GOD, !user.getOption(Option.GOD));
        boolean isGodEnabled = user.getOption(Option.GOD);

        if (isGodEnabled) {
            // GENERIC_MAX_HEALTH
            player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
            player.setFoodLevel(20);
        }

        Message messageKey = isGodEnabled ? Message.COMMAND_GOD_ENABLE : Message.COMMAND_GOD_DISABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}
