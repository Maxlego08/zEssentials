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

    /**
     * Teleports the given user to the given warp location.
     * <p>
     * The method first checks if the user has permission to access the warp, and if the
     * location is valid.
     * If the user does not have permission or the location is invalid,
     * the method sends an error message to the user and aborts.
     * <p>
     * Otherwise, the method teleports the user to the location and sends a success
     * message to the user.
     *
     * @param user the user to teleport.
     * @param warp the warp containing the location to teleport to.
     */
    public void teleport(User user, Warp warp) {

        if (!warp.hasPermission(user.getPlayer())) {
            message(user, Message.COMMAND_WARP_NO_PERMISSION, "%name%", warp.name());
            return;
        }

        if (!warp.location().isValid()) {
            message(user, Message.COMMAND_WARP_INVALID, "%name%", warp.name());
            return;
        }

        user.teleport(warp.location().getLocation(), Message.TELEPORT_MESSAGE_WARP, Message.TELEPORT_SUCCESS_WARP, "%name%", warp.name());
    }

    /**
     * Teleports the user to the given warp name.
     * If the warp does not exist, send a message to the user.
     *
     * @param user     the user to teleport
     * @param warpName the name of the warp
     */
    public void teleport(User user, String warpName) {
        Optional<Warp> optional = plugin.getWarp(warpName);
        if (optional.isEmpty()) {
            message(user, Message.COMMAND_WARP_DOESNT_EXIST, "%name%", warpName);
            return;
        }

        teleport(user, optional.get());
    }
}
