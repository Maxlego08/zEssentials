package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.user.User;

/**
 * This event is triggered when a user joins the server.
 * The user is already registered in the database at the time of this event.
 * This event is not to be confused with {@link UserFirstJoinEvent}.
 */
public class UserJoinEvent extends UserEvent {

    public UserJoinEvent(User user) {
        super(user);
    }
}
