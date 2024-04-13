package fr.maxlego08.essentials.commands.commands.warp;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Optional;

public class CommandWarp extends VCommand {

    public CommandWarp(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WarpModule.class);
        this.setPermission(Permission.ESSENTIALS_WARP);
        this.setDescription(Message.DESCRIPTION_WARP_USE);
        this.addOptionalArg("name", (sender, args) -> {
            List<Warp> warps = plugin.getWarps();
            return warps.stream().filter(warp -> warp.hasPermission(sender)).map(Warp::getName).toList();
        });
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String warpName = this.argAsString(0, null);
        WarpModule warpModule = plugin.getModuleManager().getModule(WarpModule.class);

        if (warpName == null) {

            if (warpModule.isEnableNoArgumentMessage()) {

                List<String> warps = plugin.getWarps().stream().filter(warp -> warp.hasPermission(sender)).map(warp -> getMessage(Message.COMMAND_WARP_DESTINATION, "%name%", warp.getName())).toList();
                message(sender, Message.COMMAND_WARP_USE, "%destinations%", Strings.join(warps, ','));

                return CommandResultType.DEFAULT;
            }

            if (warpModule.isEnableInventory()) {
                // TODO
                return CommandResultType.DEFAULT;
            }

            return CommandResultType.SYNTAX_ERROR;
        }

        Optional<Warp> optional = plugin.getWarp(warpName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_WARP_DOESNT_EXIST, "%name%", warpName);
            return CommandResultType.DEFAULT;
        }

        Warp warp = optional.get();
        this.user.teleport(warp.getLocation(), Message.TELEPORT_MESSAGE_WARP, Message.TELEPORT_SUCCESS_WARP, "%name%", warpName);

        return CommandResultType.SUCCESS;
    }
}
