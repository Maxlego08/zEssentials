package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.user.User;

/**
 * This event is triggered when a user first joins the server.
 * The user is not yet registered in the database at the time of this event.
 * This event is not to be confused with {@link UserJoinEvent}.
 */
public class UserFirstJoinEvent extends UserEvent {

    public UserFirstJoinEvent(User user) {
        super(user);
    }
}
