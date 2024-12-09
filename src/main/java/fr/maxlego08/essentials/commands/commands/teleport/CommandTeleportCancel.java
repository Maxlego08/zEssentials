package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportCancel extends VCommand {

    public CommandTeleportCancel(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TPA_CANCEL);
        this.setDescription(Message.DESCRIPTION_TPA_CANCEL);
        this.addRequirePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player targetPlayer = this.argAsPlayer(0);
        if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;

        this.user.cancelTeleportRequest(plugin.getStorageManager().getStorage().getUser(targetPlayer.getUniqueId()));

        return CommandResultType.SUCCESS;
    }
}
