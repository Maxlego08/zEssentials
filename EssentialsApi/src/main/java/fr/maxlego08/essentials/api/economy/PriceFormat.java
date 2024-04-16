package fr.maxlego08.essentials.api.economy;

/**
 * Represents different formats for pricing.
 */
public enum PriceFormat {

    /**
     * Represents a raw price format.
     */
    PRICE_RAW,

    /**
     * Represents a price format with decimal formatting. (10000 -> 10 000)
     */
    PRICE_WITH_DECIMAL_FORMAT,

    /**
     * Represents a price format with reduction applied. (10000 -> 10.0k)
     */
    PRICE_WITH_REDUCTION,

}

