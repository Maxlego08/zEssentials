package fr.maxlego08.essentials.commands.commands.utils.admins;

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

public class CommandPowerToolsToggle extends VCommand {
    public CommandPowerToolsToggle(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(MessageModule.class);
        this.setPermission(Permission.ESSENTIALS_POWER_TOOLS_TOGGLE);
        this.setDescription(Message.DESCRIPTION_POWER_TOOLS_TOGGLE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player == this.player || !hasPermission(sender, Permission.DESCRIPTION_POWER_TOOLS_TOGGLE_OTHER)) {
            togglePowerTools(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            togglePowerTools(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void togglePowerTools(Player player, User user, CommandSender sender) {

        user.setOption(Option.POWER_TOOLS_DISABLE, !user.getOption(Option.POWER_TOOLS_DISABLE));
        boolean isPrivateMessageDisable = user.getOption(Option.POWER_TOOLS_DISABLE);

        Message messageKey = isPrivateMessageDisable ? Message.COMMAND_POWER_TOOLS_TOGGLE_DISABLE : Message.COMMAND_POWER_TOOLS_TOGGLE_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessage() : player.getName());
    }
}
