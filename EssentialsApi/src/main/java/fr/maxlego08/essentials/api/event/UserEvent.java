package fr.maxlego08.essentials.api.event;

import fr.maxlego08.essentials.api.user.User;

public class UserEvent extends EssentialsEvent {

    private final User user;

    public UserEvent(User user) {
        this.user = user;
    }

    public UserEvent(boolean isAsync, User user) {
        super(isAsync);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
