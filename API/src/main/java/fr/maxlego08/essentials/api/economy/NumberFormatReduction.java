package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.math.BigDecimal;

/**
 * Represents a number format reduction record for reducing prices based on certain conditions.
 */
public record NumberFormatReduction(String format, BigDecimal maxAmount, String display) implements Loadable {
}
