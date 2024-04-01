package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportTo extends VCommand {

    public CommandTeleportTo(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.TPA_USE);
        this.setDescription(Message.DESCRIPTION_TPA);
        this.addRequireArg("player");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player targetPlayer = this.argAsPlayer(0);
        this.user.sendTeleportRequest(plugin.getStorageManager().getStorage().getUser(targetPlayer.getUniqueId()));

        return CommandResultType.SUCCESS;
    }
}
