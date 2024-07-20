package fr.maxlego08.essentials.api.event.events.user;

import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.event.CancellableUserEvent;
import fr.maxlego08.essentials.api.user.User;

import java.math.BigDecimal;

/**
 * This event is triggered when a user's economy is updated.
 */
public class UserEconomyUpdateEvent extends CancellableUserEvent {

    private Economy economy;
    private BigDecimal amount;

    /**
     * Constructs a new UserEconomyUpdateEvent.
     *
     * @param user    the user whose economy is being updated
     * @param economy the economy system being used
     * @param amount  the amount by which the economy is updated
     */
    public UserEconomyUpdateEvent(User user, Economy economy, BigDecimal amount) {
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
     * Sets the economy system associated with this event.
     *
     * @param economy the new economy system
     */
    public void setEconomy(Economy economy) {
        this.economy = economy;
    }

    /**
     * Gets the amount by which the user's economy is updated.
     *
     * @return the update amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount by which the user's economy is updated.
     *
     * @param amount the new update amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

