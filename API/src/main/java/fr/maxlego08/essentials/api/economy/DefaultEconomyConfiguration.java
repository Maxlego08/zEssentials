package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Loadable;

import java.math.BigDecimal;

public record DefaultEconomyConfiguration(String economy, BigDecimal amount) implements Loadable {
}
