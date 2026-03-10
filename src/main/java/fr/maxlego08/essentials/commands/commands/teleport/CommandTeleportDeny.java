package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class CommandTeleportDeny extends VCommand {

    public CommandTeleportDeny(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TPA_DENY);
        this.setDescription(Message.DESCRIPTION_TPA_DENY);
        this.addOptionalArg("player");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String arg = this.argAsString(0, null);

        if (arg == null) {
            // deny latest request
            TeleportRequest teleportRequest = this.user.getTeleportRequest();
            if (teleportRequest == null) {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }

            if (!teleportRequest.isValid()) {
                this.user.removeIncomingTeleportRequest(teleportRequest.getFromUser());
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE_2);
                return CommandResultType.DEFAULT;
            }

            teleportRequest.deny();
            this.user.removeIncomingTeleportRequest(teleportRequest.getFromUser());

        } else if (arg.equals("*")) {
            // deny all pending requests
            Collection<TeleportRequest> requests = this.user.getIncomingTeleportRequests();
            if (requests.isEmpty()) {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }

            int count = 0;
            for (TeleportRequest request : new ArrayList<>(requests)) {
                if (request.isValid()) {
                    request.deny();
                    this.user.removeIncomingTeleportRequest(request.getFromUser());
                    count++;
                }
            }

            if (count > 0) {
                message(sender, Message.COMMAND_TPA_DENY_ALL_SUCCESS, "%count%", count);
            } else {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }

        } else {
            // deny request from that player
            Player targetPlayer = this.argAsPlayer(0);
            User targetUser = null;

            if (targetPlayer != null) {
                targetUser = plugin.getStorageManager().getStorage().getUser(targetPlayer.getUniqueId());
            } else {
                OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
                if (offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {
                    targetUser = plugin.getStorageManager().getStorage().getUser(offlinePlayer.getUniqueId());
                }
            }

            if (targetUser == null) {
                message(sender, Message.PLAYER_NOT_FOUND, "%player%", arg);
                return CommandResultType.DEFAULT;
            }

            TeleportRequest teleportRequest = this.user.getTeleportRequestFrom(targetUser);
            if (teleportRequest == null) {
                message(sender, Message.COMMAND_TPA_ERROR_NO_REQUEST_FROM_PLAYER, "%player%", targetUser.getName());
                return CommandResultType.DEFAULT;
            }

            if (!teleportRequest.isValid()) {
                this.user.removeIncomingTeleportRequest(targetUser);
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE_2);
                return CommandResultType.DEFAULT;
            }

            teleportRequest.deny();
            this.user.removeIncomingTeleportRequest(targetUser);
        }

        return CommandResultType.SUCCESS;
    }
}
