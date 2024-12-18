package fr.maxlego08.essentials.commands.commands.warp;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

public class CommandWarps extends VCommand {

    public CommandWarps(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WarpModule.class);
        this.setPermission(Permission.ESSENTIALS_WARPS);
        this.setDescription(Message.DESCRIPTION_WARP_LIST);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        WarpModule warpModule = plugin.getModuleManager().getModule(WarpModule.class);
        if (warpModule.isEnableInventory()) {
            plugin.getInventoryManager().openInventory(this.player, plugin, "warps");
            return CommandResultType.DEFAULT;
        }

        List<String> warps = plugin.getWarps().stream().filter(warp -> warp.hasPermission(sender)).map(warp -> getMessage(Message.COMMAND_WARP_LIST_INFO, "%name%", warp.name())).toList();
        message(sender, Message.COMMAND_WARP_LIST, "%destinations%", Strings.join(warps, ','));

        return CommandResultType.SUCCESS;
    }
}
