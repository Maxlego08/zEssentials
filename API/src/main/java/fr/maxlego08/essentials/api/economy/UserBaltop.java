package fr.maxlego08.essentials.api.economy;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a user in the balance top list.
 */
public interface UserBaltop {

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return the UUID of the user
     */
    UUID getUniqueId();

    /**
     * Retrieves the name of the user.
     *
     * @return the name of the user
     */
    String getName();

    /**
     * Retrieves the amount associated with the user.
     *
     * @return the amount as a BigDecimal
     */
    BigDecimal getAmount();

    /**
     * Retrieves the position of the user in the balance top list.
     *
     * @return the position of the user as a long
     */
    long getPosition();
}
