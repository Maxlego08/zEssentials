package fr.maxlego08.essentials.api.event.events;

import fr.maxlego08.essentials.api.User;
import fr.maxlego08.essentials.api.event.UserEvent;

public class UserFirstJoinEvent extends UserEvent {

    public UserFirstJoinEvent(User user) {
        super(user);
    }
}
