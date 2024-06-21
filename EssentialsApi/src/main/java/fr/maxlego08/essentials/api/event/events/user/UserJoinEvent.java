package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.user.User;

public class UserJoinEvent extends UserEvent {

    public UserJoinEvent(User user) {
        super(user);
    }
}
