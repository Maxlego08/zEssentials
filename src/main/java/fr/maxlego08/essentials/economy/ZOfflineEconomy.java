package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.OfflineEconomy;

import java.math.BigDecimal;
import java.util.Map;

public class ZOfflineEconomy implements OfflineEconomy {

    private final Map<String, BigDecimal> economies;

    public ZOfflineEconomy(Map<String, BigDecimal> economies) {
        this.economies = economies;
    }

    @Override
    public BigDecimal getEconomy(String economyName) {
        return this.economies.getOrDefault(economyName, BigDecimal.ZERO);
    }
}
