package fr.maxlego08.essentials.user;

import com.tcoded.folialib.impl.PlatformScheduler;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Location;

import java.util.concurrent.atomic.AtomicInteger;

public class ZTeleportRequest extends ZUtils implements TeleportRequest {

    private final EssentialsPlugin plugin;
    private final User toUser;
    private final User fromUser;
    private final long expiredAt;
    private boolean isTeleport = false;

    public ZTeleportRequest(EssentialsPlugin plugin, User toUser, User fromUser, long expiredAt) {
        this.plugin = plugin;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.expiredAt = expiredAt;
    }

    @Override
    public User getToUser() {
        return this.toUser;
    }

    @Override
    public User getFromUser() {
        return this.fromUser;
    }

    @Override
    public long getExpiredAt() {
        return this.expiredAt;
    }

    @Override
    public boolean isValid() {
        return this.expiredAt > System.currentTimeMillis() && this.toUser.isOnline() && this.fromUser.isOnline() && !isTeleport;
    }

    @Override
    public void accept() {

        // Check if both players are online
        if (!this.fromUser.isOnline() || !this.toUser.isOnline()) {
            this.isTeleport = true;
            return;
        }

        message(this.fromUser, Message.COMMAND_TPA_ACCEPT_SENDER, this.toUser);
        message(this.toUser, Message.COMMAND_TPA_ACCEPT_RECEIVER, this.fromUser);

        // Validate player objects
        if (this.fromUser.getPlayer() == null || this.toUser.getPlayer() == null) {
            this.isTeleport = true;
            return;
        }

        TeleportationModule teleportationModule = this.plugin.getModuleManager().getModule(TeleportationModule.class);
        AtomicInteger atomicInteger = new AtomicInteger(teleportationModule.getTeleportDelay(fromUser.getPlayer()));

        if (teleportationModule.isTeleportDelayBypass() && this.fromUser.hasPermission(Permission.ESSENTIALS_TELEPORT_BYPASS) || atomicInteger.get() <= 0) {
            this.teleport(teleportationModule);
            return;
        }

        Location playerLocation = fromUser.getPlayer().getLocation();

        PlatformScheduler serverImplementation = this.plugin.getScheduler();
        serverImplementation.runAtLocationTimer(this.toUser.getPlayer().getLocation(), wrappedTask -> {

            // Check if players are still online
            if (!this.toUser.isOnline() || !this.fromUser.isOnline()) {
                wrappedTask.cancel();
                this.fromUser.removeTeleportRequest(this.toUser);
                return;
            }

            // Validate player objects
            if (this.fromUser.getPlayer() == null || this.toUser.getPlayer() == null) {
                wrappedTask.cancel();
                this.fromUser.removeTeleportRequest(this.toUser);
                return;
            }

            if (!same(playerLocation, fromUser.getPlayer().getLocation())) {
                message(this.fromUser, Message.TELEPORT_MOVE);
                wrappedTask.cancel();
                this.fromUser.removeTeleportRequest(this.toUser);
                return;
            }

            int currentSecond = atomicInteger.getAndDecrement();

            if (currentSecond <= 0) {
                wrappedTask.cancel();
                this.teleport(teleportationModule);
            } else {
                message(this.fromUser, Message.TELEPORT_MESSAGE, "%seconds%", currentSecond);
            }

        }, 1, 20);
    }

    private void teleport(TeleportationModule teleportationModule) {
        // Validate both players are still online and have valid player objects
        if (!this.toUser.isOnline() || !this.fromUser.isOnline()) {
            this.isTeleport = true;
            return;
        }

        if (this.fromUser.getPlayer() == null || this.toUser.getPlayer() == null) {
            this.isTeleport = true;
            return;
        }

        Location playerLocation = toUser.getPlayer().getLocation();
        Location location = fromUser.getPlayer().isFlying() ? playerLocation : teleportationModule.isTeleportSafety() ? toSafeLocation(playerLocation) : playerLocation;

        if (teleportationModule.isTeleportToCenter()) {
            location = location.getBlock().getLocation().add(0.5, 0, 0.5);
            location.setYaw(playerLocation.getYaw());
            location.setPitch(playerLocation.getPitch());
        }

        this.fromUser.teleportNow(location);
        this.fromUser.removeTeleportRequest(this.toUser);

        message(this.fromUser, Message.TELEPORT_SUCCESS);
        this.isTeleport = true;
    }

    @Override
    public void deny() {

        message(this.fromUser, Message.COMMAND_TP_DENY_RECEIVER, this.toUser);
        message(this.toUser, Message.COMMAND_TP_DENY_SENDER, this.fromUser);
        this.isTeleport = true;

    }
}
