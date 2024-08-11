package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandSudo extends VCommand {

    public CommandSudo(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_SUDO);
        this.setDescription(Message.DESCRIPTION_SUDO);
        this.addRequireArg("player");
        this.addRequireArg("cmg/msg", (a, b) -> Arrays.asList("cmd", "msg"));
        this.addRequireArg("content");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String type = this.argAsString(1);
        String message = getArgs(2);

        if (hasPermission(player, Permission.ESSENTIALS_SUDO_BYPASS)) {
            message(sender, Message.COMMAND_SUDO_ERROR);
            return CommandResultType.DEFAULT;
        }

        if (type.equalsIgnoreCase("msg")) {

            player.chat(message);
            message(sender, Message.COMMAND_SUDO_MESSAGE, "%player%", player.getName(), "%message%", message);

        } else {

            player.performCommand(message);
            message(sender, Message.COMMAND_SUDO_COMMAND, "%player%", player.getName(), "%command%", message);
        }

        return CommandResultType.SUCCESS;
    }
}
