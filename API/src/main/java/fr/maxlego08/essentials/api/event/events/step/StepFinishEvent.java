package fr.maxlego08.essentials.api.event.events.step;

import fr.maxlego08.essentials.api.event.CancellableUserEvent;
import fr.maxlego08.essentials.api.steps.PlayerStep;
import fr.maxlego08.essentials.api.user.User;

/**
 * This event is triggered when a step is finished by a user.
 */
public class StepFinishEvent extends CancellableUserEvent {

    private final PlayerStep step;

    public StepFinishEvent(User user, PlayerStep step) {
        super(user);
        this.step = step;
    }

    public StepFinishEvent(User user, boolean isCancelled, PlayerStep step) {
        super(user, isCancelled);
        this.step = step;
    }

    public StepFinishEvent(boolean isAsync, User user, PlayerStep step) {
        super(isAsync, user);
        this.step = step;
    }

    public PlayerStep getStep() {
        return step;
    }
}
