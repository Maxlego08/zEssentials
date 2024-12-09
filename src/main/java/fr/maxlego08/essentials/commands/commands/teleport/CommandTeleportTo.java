package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportTo extends VCommand {

    public CommandTeleportTo(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TPA);
        this.setDescription(Message.DESCRIPTION_TPA);
        this.addRequirePlayerNameArg();
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player targetPlayer = this.argAsPlayer(0);
        if (targetPlayer == null) return CommandResultType.SYNTAX_ERROR;

        User targetUser = plugin.getStorageManager().getStorage().getUser(targetPlayer.getUniqueId());

        if (targetUser.getUniqueId().equals(this.player.getUniqueId())) {
            message(this.sender, Message.COMMAND_TPA_ERROR_SAME);
            return CommandResultType.DEFAULT;
        }

        TeleportationModule teleportationModule = plugin.getModuleManager().getModule(TeleportationModule.class);
        if (teleportationModule.isOpenConfirmInventoryForTpa()) {
            this.user.setTargetUser(targetUser);
            teleportationModule.openConfirmInventory(player);
            return CommandResultType.SUCCESS;
        }

        this.user.sendTeleportRequest(targetUser);

        return CommandResultType.SUCCESS;
    }
}
