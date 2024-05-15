package fr.maxlego08.essentials.api.event;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EssentialsEvent extends Event {

    private final static HandlerList handlers = new HandlerList();

    public EssentialsEvent() {
    }

    public EssentialsEvent(boolean isAsync) {
        super(isAsync);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void callEvent(EssentialsPlugin plugin) {
        plugin.getScheduler().runNextTick(wrappedTask -> this.callEvent());
    }
}
