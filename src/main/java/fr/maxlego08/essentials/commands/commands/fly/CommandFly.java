package fr.maxlego08.essentials.commands.commands.fly;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandFly extends VCommand {
    public CommandFly(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_FLY);
        this.setDescription(Message.DESCRIPTION_FLY);
        this.addSubCommand(new CommandFlyAdd(plugin));
        this.addSubCommand(new CommandFlyRemove(plugin));
        this.addSubCommand(new CommandFlySet(plugin));
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);

        if (this.player == null) {
            return CommandResultType.SYNTAX_ERROR;
        }

        if (!hasPermission(sender, Permission.ESSENTIALS_FLY_OTHER)) {
            player = this.player;
        }

        if (hasPermission(sender, Permission.ESSENTIALS_FLY_UNLIMITED)) {
            player.setAllowFlight(!player.getAllowFlight());
            player.setFlying(player.getAllowFlight());
            message(sender, player.getAllowFlight() ? Message.COMMAND_FLY_ENABLE : Message.COMMAND_FLY_DISABLE, "%player%", this.sender == player ? Message.YOU.getMessageAsString() : player.getName());

        } else {

            User user = plugin.getUser(player.getUniqueId());
            if (user == null) return CommandResultType.SYNTAX_ERROR;

            if (user.getFlySeconds() <= 0) {
                message(sender, sender == player ? Message.COMMAND_FLY_ERROR : Message.COMMAND_FLY_ERROR_OTHER, player);
                return CommandResultType.DEFAULT;
            }

            player.setAllowFlight(!player.getAllowFlight());
            player.setFlying(player.getAllowFlight());
            message(sender, player.getAllowFlight() ? Message.COMMAND_FLY_ENABLE_SECONDS : Message.COMMAND_FLY_DISABLE, "%player%", this.sender == player ? Message.YOU.getMessageAsString() : player.getName(), "%time%", TimerBuilder.getStringTime(user.getFlySeconds() * 1000));

        }

        return CommandResultType.SUCCESS;
    }
}
