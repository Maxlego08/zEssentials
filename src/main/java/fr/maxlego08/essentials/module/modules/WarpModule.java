package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.Warp;
import fr.maxlego08.essentials.module.ZModule;

import java.util.Optional;

public class WarpModule extends ZModule {

    private boolean enableInventory;
    private boolean enableNoArgumentMessage;

    public WarpModule(ZEssentialsPlugin plugin) {
        super(plugin, "warp");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("warps");
    }

    public boolean isEnableInventory() {
        return enableInventory;
    }

    public boolean isEnableNoArgumentMessage() {
        return enableNoArgumentMessage;
    }

    public void teleport(User user, Warp warp) {

        if (!warp.location().isValid()) {
            message(user, Message.COMMAND_WARP_INVALID, "%name%", warp.name());
            return;
        }

        user.teleport(warp.location().getLocation(), Message.TELEPORT_MESSAGE_WARP, Message.TELEPORT_SUCCESS_WARP, "%name%", warp.name());
    }

    public void teleport(User user, String warpName) {
        Optional<Warp> optional = plugin.getWarp(warpName);
        if (optional.isEmpty()) {
            message(user, Message.COMMAND_WARP_DOESNT_EXIST, "%name%", warpName);
            return;
        }

        teleport(user, optional.get());
    }
}
