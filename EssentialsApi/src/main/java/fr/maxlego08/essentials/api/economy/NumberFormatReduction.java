package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.math.BigDecimal;

public record NumberFormatReduction(String format, BigDecimal maxAmount, String display) implements Loadable {
}
