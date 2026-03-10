package fr.maxlego08.essentials.commands.commands.deathmessage;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.DeathMessageModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDeathMessageToggle extends VCommand {

    public CommandDeathMessageToggle(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(DeathMessageModule.class);
        this.setPermission(Permission.ESSENTIALS_DEATH_MESSAGE_TOGGLE);
        this.setDescription(Message.DESCRIPTION_DEATH_MESSAGE_TOGGLE);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (player.equals(this.player) || !hasPermission(sender, Permission.ESSENTIALS_DEATH_MESSAGE_TOGGLE_OTHER)) {
            toggleDeathMessage(player, this.user, sender);
        } else {
            User otherUser = getUser(player);
            if (otherUser == null) return CommandResultType.SYNTAX_ERROR;
            toggleDeathMessage(player, otherUser, sender);
        }

        return CommandResultType.SUCCESS;
    }

    private void toggleDeathMessage(Player player, User user, CommandSender sender) {
        user.setOption(Option.DEATH_MESSAGE_DISABLE, !user.getOption(Option.DEATH_MESSAGE_DISABLE));
        boolean isDeathMessageDisable = user.getOption(Option.DEATH_MESSAGE_DISABLE);

        Message messageKey = isDeathMessageDisable ? Message.COMMAND_DEATH_MESSAGE_TOGGLE_DISABLE : Message.COMMAND_DEATH_MESSAGE_TOGGLE_ENABLE;
        message(sender, messageKey, "%player%", user == this.user ? Message.YOU.getMessageAsString() : player.getName());
    }
}
