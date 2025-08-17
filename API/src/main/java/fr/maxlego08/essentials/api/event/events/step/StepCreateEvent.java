package fr.maxlego08.essentials.api.event.events.step;

import fr.maxlego08.essentials.api.event.CancellableUserEvent;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.api.user.User;

/**
 * This event is triggered when a new step is created for a user.
 */
public class StepCreateEvent extends CancellableUserEvent {

    private final Step step;

    public StepCreateEvent(User user, Step step) {
        super(user);
        this.step = step;
    }

    public Step getStep() {
        return step;
    }
}
