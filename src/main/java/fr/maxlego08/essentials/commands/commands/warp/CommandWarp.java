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

public class CommandWarp extends VCommand {

    public CommandWarp(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WarpModule.class);
        this.setPermission(Permission.ESSENTIALS_WARP);
        this.setDescription(Message.DESCRIPTION_WARP_USE);
        this.addOptionalArg("name", (sender, args) -> {
            List<Warp> warps = plugin.getWarps();
            return warps.stream().filter(warp -> warp.hasPermission(sender)).map(Warp::name).toList();
        });
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String warpName = this.argAsString(0, null);
        WarpModule warpModule = plugin.getModuleManager().getModule(WarpModule.class);

        if (warpName == null) {

            if (warpModule.isEnableInventory()) {
                plugin.getInventoryManager().openInventory(this.player, plugin, "warps");
                return CommandResultType.DEFAULT;
            }

            if (warpModule.isEnableNoArgumentMessage()) {

                List<String> warps = plugin.getWarps().stream().filter(warp -> warp.hasPermission(sender)).map(warp -> getMessage(Message.COMMAND_WARP_DESTINATION, "%name%", warp.name())).toList();

                if (warps.isEmpty()) {
                    message(sender, Message.COMMAND_WARP_EMPTY);
                    return CommandResultType.DEFAULT;
                }

                message(sender, Message.COMMAND_WARP_USE, "%destinations%", Strings.join(warps, ','));

                return CommandResultType.SUCCESS;
            }

            return CommandResultType.SYNTAX_ERROR;
        }

        warpModule.teleport(this.user, warpName);
        return CommandResultType.SUCCESS;
    }
}
