package fr.maxlego08.essentials.api.event;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.event.Cancellable;

public class CancellableUserEvent extends UserEvent implements Cancellable {

    private boolean isCancelled;

    public CancellableUserEvent(User user) {
        super(user);
    }

    public CancellableUserEvent(User user, boolean isCancelled) {
        super(user);
        this.isCancelled = isCancelled;
    }

    public CancellableUserEvent(boolean isAsync, User user) {
        super(isAsync, user);
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
