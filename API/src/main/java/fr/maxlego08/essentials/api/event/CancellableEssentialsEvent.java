package fr.maxlego08.essentials.api.event;

import org.bukkit.event.Cancellable;

public class CancellableEssentialsEvent extends EssentialsEvent implements Cancellable {

    private boolean isCancelled;

    public CancellableEssentialsEvent() {
        this(false);
    }

    public CancellableEssentialsEvent(boolean isAsync) {
        super(isAsync);
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
