package fr.maxlego08.essentials.commands.commands.economy;

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

public class CommandPayToggle extends VCommand {
    public CommandPayToggle(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_PAY_TOGGLE);
        this.setDescription(Message.DESCRIPTION_PAY_TOGGLE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player || !hasPermission(sender, Permission.ESSENTIALS_PAY_TOGGLE_OTHER)) {
            togglePay(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            togglePay(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void togglePay(Player player, User user, CommandSender sender) {

        user.setOption(Option.PAY_DISABLE, !user.getOption(Option.PAY_DISABLE));
        boolean isPayDisable = user.getOption(Option.PAY_DISABLE);

        Message messageKey = isPayDisable ? Message.COMMAND_PAY_TOGGLE_DISABLE : Message.COMMAND_PAY_TOGGLE_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}
