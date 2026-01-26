package fr.maxlego08.essentials.commands.commands.utils.admins;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandVanish extends VCommand {

    public CommandVanish(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_VANISH);
        this.setDescription(Message.DESCRIPTION_VANISH);
        this.addOptionalArg("player", getVisiblePlayerNames());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);
        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        boolean self = player.equals(this.player);
        if (!self && !hasPermission(this.sender, Permission.ESSENTIALS_VANISH_OTHER)) {
            message(this.sender, Message.COMMAND_NO_PERMISSION);
            return CommandResultType.NO_PERMISSION;
        }

        User user = self ? this.user : getUser(player);
        if (user == null) {
            message(this.sender, Message.PLAYER_NOT_FOUND, "%player%", player.getName());
            return CommandResultType.DEFAULT;
        }

        boolean vanish = !user.getOption(Option.VANISH);
        user.setOption(Option.VANISH, vanish);
        updateVanishState(plugin, player, vanish);

        Message messageKey = vanish ? Message.COMMAND_VANISH_ENABLE : Message.COMMAND_VANISH_DISABLE;
        if (self) {
            message(this.sender, messageKey, "%player%", Message.YOU.getMessageAsString());
        } else {
            message(this.sender, messageKey, "%player%", player.getName());
            message(player, messageKey, "%player%", Message.YOU.getMessageAsString());
        }

        return CommandResultType.SUCCESS;
    }
}
