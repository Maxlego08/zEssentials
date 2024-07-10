package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.user.User;

public class UserQuitEvent extends UserEvent {

    public UserQuitEvent(User user) {
        super(user);
    }
}
