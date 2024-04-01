package fr.maxlego08.essentials.api.event;

import fr.maxlego08.essentials.api.User;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserEvent extends Event {

    private final static HandlerList handlers = new HandlerList();
    private final User user;

    public UserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
