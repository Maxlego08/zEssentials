package fr.maxlego08.essentials.api.economy;

import java.math.BigDecimal;

/**
 * Represents an economy system in the plugin.
 */
public interface Economy {

    /**
     * Gets the name of the economy system.
     *
     * @return The name of the economy system.
     */
    String getName();

    /**
     * Gets the display name of the economy system.
     *
     * @return The display name of the economy system.
     */
    String getDisplayName();

    /**
     * Gets the symbol used for the currency of the economy system.
     *
     * @return The symbol used for the currency.
     */
    String getSymbol();

    /**
     * Gets the format used for displaying currency amounts.
     *
     * @return The format used for displaying currency amounts.
     */
    String getFormat();

    /**
     * Checks if the economy system is using Vault.
     *
     * @return true if the economy system is using Vault, false otherwise.
     */
    boolean isVaultEconomy();

    /**
     * Gets the minimum value allowed for currency amounts.
     *
     * @return The minimum value allowed.
     */
    BigDecimal getMinValue();

    /**
     * Gets the maximum value allowed for currency amounts.
     *
     * @return The maximum value allowed.
     */
    BigDecimal getMaxValue();

    /**
     * Gets the minimum value allowed for payments.
     *
     * @return The minimum value allowed for payments.
     */
    BigDecimal getMinPayValue();

    /**
     * Gets the maximum value allowed for payments.
     *
     * @return The maximum value allowed for payments.
     */
    BigDecimal getMaxPayValue();

    /**
     * Gets the minimum value for confirming payments in the inventory.
     *
     * @return The minimum value for confirming payments in the inventory.
     */
    BigDecimal getMinConfirmInventory();

    /**
     * Checks if payments are enabled for the economy system.
     *
     * @return true if payments are enabled, false otherwise.
     */
    boolean isPaymentEnabled();

    /**
     * Checks if confirming payments in the inventory is enabled for the economy system.
     *
     * @return true if confirming payments in the inventory is enabled, false otherwise.
     */
    boolean isConfirmInventoryEnabled();

    /**
     * Gets the price format used for formatting currency amounts.
     *
     * @return The price format used for formatting currency amounts.
     */
    PriceFormat getPriceFormat();

    /**
     * Formats the specified price as a string according to the economy format and amount.
     *
     * @param priceAsString The price as a string.
     * @param amount        The amount.
     * @return The formatted price string.
     */
    default String format(String priceAsString, long amount) {
        return getFormat().replace("%price%", priceAsString).replace("%s%", amount > 1 ? "s" : "");
    }

}
