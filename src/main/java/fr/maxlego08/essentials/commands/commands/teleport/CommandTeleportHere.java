package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportHere extends VCommand {

    public CommandTeleportHere(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TP_HERE);
        this.setDescription(Message.DESCRIPTION_TP_SELF);
        this.addRequirePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player targetPlayer = this.argAsPlayer(0);
        if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;

        var user = getUser(targetPlayer);
        if (user == null) return CommandResultType.SYNTAX_ERROR;

        user.teleportNow(player.getLocation());
        message(this.sender, Message.COMMAND_TP_SELF, targetPlayer);

        return CommandResultType.SUCCESS;
    }
}
