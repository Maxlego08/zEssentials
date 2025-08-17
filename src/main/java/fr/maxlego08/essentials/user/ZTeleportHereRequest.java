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

public class ZTeleportHereRequest extends ZUtils implements TeleportRequest {

    private final EssentialsPlugin plugin;
    private final User toUser;
    private final User fromUser;
    private final long expiredAt;
    private boolean isTeleport = false;

    public ZTeleportHereRequest(EssentialsPlugin plugin, User toUser, User fromUser, long expiredAt) {
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

        message(this.fromUser, Message.COMMAND_TPA_HERE_ACCEPT_SENDER, this.toUser);
        message(this.toUser, Message.COMMAND_TPA_HERE_ACCEPT_RECEIVER, this.fromUser);

        TeleportationModule teleportationModule = this.plugin.getModuleManager().getModule(TeleportationModule.class);
        AtomicInteger atomicInteger = new AtomicInteger(teleportationModule.getTeleportDelay(toUser.getPlayer()));

        if (teleportationModule.isTeleportDelayBypass() && this.toUser.hasPermission(Permission.ESSENTIALS_TELEPORT_BYPASS) || atomicInteger.get() <= 0) {
            this.teleport(teleportationModule);
            return;
        }

        Location playerLocation = toUser.getPlayer().getLocation();

        PlatformScheduler serverImplementation = this.plugin.getScheduler();
        serverImplementation.runAtLocationTimer(this.fromUser.getPlayer().getLocation(), wrappedTask -> {

            if (!same(playerLocation, toUser.getPlayer().getLocation())) {

                message(this.toUser, Message.TELEPORT_MOVE);
                wrappedTask.cancel();
                this.fromUser.removeTeleportRequest(this.toUser);
                return;
            }

            int currentSecond = atomicInteger.getAndDecrement();

            if (!this.toUser.isOnline() || !this.fromUser.isOnline()) {
                wrappedTask.cancel();
                return;
            }

            if (currentSecond <= 0) {

                wrappedTask.cancel();
                this.teleport(teleportationModule);
            } else {

                message(this.toUser, Message.TELEPORT_MESSAGE, "%seconds%", currentSecond);
            }

        }, 1, 20);
    }

    private void teleport(TeleportationModule teleportationModule) {
        Location playerLocation = fromUser.getPlayer().getLocation();
        Location location = toUser.getPlayer().isFlying() ? playerLocation : teleportationModule.isTeleportSafety() ? toSafeLocation(playerLocation) : playerLocation;

        if (teleportationModule.isTeleportToCenter()) {
            location = location.getBlock().getLocation().add(0.5, 0, 0.5);
            location.setYaw(playerLocation.getYaw());
            location.setPitch(playerLocation.getPitch());
        }

        this.toUser.teleportNow(location);
        this.toUser.removeTeleportRequest(this.toUser);

        message(this.toUser, Message.TELEPORT_SUCCESS);
        this.isTeleport = true;
    }

    @Override
    public void deny() {

        message(this.fromUser, Message.COMMAND_TP_DENY_RECEIVER, this.toUser);
        message(this.toUser, Message.COMMAND_TP_DENY_SENDER, this.fromUser);
        this.isTeleport = true;

    }
}
