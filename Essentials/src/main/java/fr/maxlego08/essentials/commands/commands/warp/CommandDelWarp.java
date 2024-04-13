package fr.maxlego08.essentials.commands.commands.warp;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.module.modules.WarpModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

import java.util.Arrays;
import java.util.Optional;

public class CommandDelWarp extends VCommand {

    public CommandDelWarp(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WarpModule.class);
        this.setPermission(Permission.ESSENTIALS_WARP_DEL);
        this.setDescription(Message.DESCRIPTION_WARP_DEL);
        this.addRequireArg("name", (a, b) -> plugin.getWarps().stream().map(Warp::getName).toList());
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        String warpName = this.argAsString(0);

        Optional<Warp> optional = plugin.getWarp(warpName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_WARP_DOESNT_EXIST, "%name%", warpName);
            return CommandResultType.DEFAULT;
        }

        ConfigStorage.warps.removeIf(warp -> warp.getName().equalsIgnoreCase(warpName));
        ConfigStorage.getInstance().save(plugin.getPersist());

        message(sender, Message.COMMAND_WARP_CREATE, "%name%", warpName);

        return CommandResultType.SUCCESS;
    }
}
