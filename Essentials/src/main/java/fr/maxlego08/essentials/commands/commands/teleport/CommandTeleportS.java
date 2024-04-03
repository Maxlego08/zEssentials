package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportS extends VCommand {

    public CommandTeleportS(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_TP);
        this.setDescription(Message.DESCRIPTION_TP_SELF);
        this.addRequireArg("player");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player targetPlayer = this.argAsPlayer(0);
        getUser(targetPlayer).teleport(player.getLocation());
        message(this.sender, Message.COMMAND_TP_SELF, targetPlayer);

        return CommandResultType.SUCCESS;
    }
}
