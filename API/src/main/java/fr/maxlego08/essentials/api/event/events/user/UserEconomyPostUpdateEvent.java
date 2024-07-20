package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.event.UserEvent;
import fr.maxlego08.essentials.api.user.User;

import java.math.BigDecimal;

/**
 * This event is triggered when a user's economy is updated.
 */
public class UserEconomyPostUpdateEvent extends UserEvent {

    private final Economy economy;
    private final BigDecimal amount;

    /**
     * Constructs a new UserEconomyUpdateEvent.
     *
     * @param user    the user whose economy is being updated
     * @param economy the economy system being used
     * @param amount  the amount by which the economy is updated
     */
    public UserEconomyPostUpdateEvent(User user, Economy economy, BigDecimal amount) {
        super(user);
        this.economy = economy;
        this.amount = amount;
    }

    /**
     * Gets the economy system associated with this event.
     *
     * @return the economy system
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Gets the amount by which the user's economy is updated.
     *
     * @return the update amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
}

