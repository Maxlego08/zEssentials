package fr.maxlego08.essentials.user;

import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Location;

import java.util.concurrent.TimeUnit;
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

        message(this.fromUser, Message.COMMAND_TPA_ACCEPT_SENDER, "%player%", this.toUser.getName());
        message(this.toUser, Message.COMMAND_TPA_ACCEPT_RECEIVER, "%player%", this.fromUser.getName());

        // ToDo, Add configuration and bypass permission

        Location playerLocation = fromUser.getPlayer().getLocation();
        AtomicInteger verif = new AtomicInteger(2);

        ServerImplementation serverImplementation = this.plugin.getScheduler();
        serverImplementation.runAtLocationTimer(this.toUser.getPlayer().getLocation(), wrappedTask -> {

            if (!same(playerLocation, fromUser.getPlayer().getLocation())) {
                message(this.fromUser, Message.TELEPORT_MOVE);
                wrappedTask.cancel();
                this.fromUser.removeTeleportRequest(this.toUser);
                return;
            }

            int currentSecond = verif.getAndDecrement();

            if (!this.toUser.isOnline() || !this.fromUser.isOnline()) {
                wrappedTask.cancel();
                return;
            }

            if (currentSecond == 0) {

                wrappedTask.cancel();

                Location location = fromUser.getPlayer().isFlying() ? toUser.getPlayer().getLocation() : toSafeLocation(toUser.getPlayer().getLocation());

                this.fromUser.teleport(location);
                this.fromUser.removeTeleportRequest(this.toUser);

                message(this.fromUser, Message.TELEPORT_SUCCESS);
                this.isTeleport = true;

            } else {
                message(this.fromUser, Message.TELEPORT_MESSAGE, "%seconds%", currentSecond);
            }

        }, 0, 1, TimeUnit.SECONDS);

    }
}
