package fr.maxlego08.essentials.commands.commands.messages;

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

public class CommandMessageToggle extends VCommand {
    public CommandMessageToggle(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_MESSAGE_TOGGLE);
        this.setDescription(Message.DESCRIPTION_MESSAGE_TOGGLE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player || !hasPermission(sender, Permission.DESCRIPTION_MESSAGE_TOGGLE_OTHER)) {
            togglePrivateMessage(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            togglePrivateMessage(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void togglePrivateMessage(Player player, User user, CommandSender sender) {

        user.setOption(Option.PRIVATE_MESSAGE_DISABLE, !user.getOption(Option.PRIVATE_MESSAGE_DISABLE));
        boolean isPrivateMessageDisable = user.getOption(Option.PRIVATE_MESSAGE_DISABLE);

        Message messageKey = isPrivateMessageDisable ? Message.COMMAND_MESSAGE_TOGGLE_DISABLE : Message.COMMAND_MESSAGE_TOGGLE_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessage() : player.getName());
    }
}
