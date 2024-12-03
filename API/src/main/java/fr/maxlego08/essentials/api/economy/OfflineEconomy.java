package fr.maxlego08.essentials.api.economy;

import org.jetbrains.annotations.NotNull;

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
    @NotNull BigDecimal getEconomy(String economyName);

    /**
     * Deposits the specified amount of currency into the specified economy for an offline user.
     *
     * @param name   the name of the economy to deposit currency into
     * @param amount the amount of currency to deposit
     */
    void deposit(String name, BigDecimal amount);

    /**
     * Withdraws the specified amount of currency from the specified economy for an offline user.
     *
     * @param name   the name of the economy to withdraw currency from
     * @param amount the amount of currency to withdraw
     */
    void withdraw(String name, BigDecimal amount);

    /**
     * Sets the balance of the specified economy for an offline user to the specified amount.
     *
     * @param name   the name of the economy to set the balance for
     * @param amount the new balance
     */
    void set(String name, BigDecimal amount);
}

