package fr.maxlego08.essentials.api.economy;

import java.math.BigDecimal;

/**
 * Interface for managing offline economy-related operations.
 * Provides methods to retrieve economy information for offline users.
 */
public interface OfflineEconomy {

    /**
     * Retrieves the balance of a specified economy for an offline user.
     *
     * @param economyName the name of the economy to retrieve the balance from
     * @return the balance of the specified economy as a BigDecimal
     */
    BigDecimal getEconomy(String economyName);

}

