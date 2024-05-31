package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.math.BigDecimal;

/**
 * Represents a number multiplication format record for multiplying prices based on certain conditions.
 */
public record NumberMultiplicationFormat(String format, BigDecimal multiplication) implements Loadable {
}
