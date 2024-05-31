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

public class CommandSocialSpy extends VCommand {
    public CommandSocialSpy(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_SOCIALSPY);
        this.setDescription(Message.DESCRIPTION_SOCIALSPY);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player) {
            toggleSocialspy(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            toggleSocialspy(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void toggleSocialspy(Player player, User user, CommandSender sender) {

        user.setOption(Option.SOCIAL_SPY, !user.getOption(Option.SOCIAL_SPY));
        boolean isPrivateMessageDisable = user.getOption(Option.SOCIAL_SPY);

        Message messageKey = isPrivateMessageDisable ? Message.COMMAND_SOCIAL_SPY_ENABLE : Message.COMMAND_SOCIAL_SPY_DISABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessage() : player.getName());
    }
}
