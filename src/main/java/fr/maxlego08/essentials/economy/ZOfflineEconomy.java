package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.OfflineEconomy;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class ZOfflineEconomy implements OfflineEconomy {

    private final Map<String, BigDecimal> economies;

    public ZOfflineEconomy(Map<String, BigDecimal> economies) {
        this.economies = economies;
    }

    @Override
    public @NotNull BigDecimal getEconomy(String economyName) {
        return this.economies.getOrDefault(economyName, BigDecimal.ZERO);
    }

    @Override
    public void deposit(String name, BigDecimal amount) {
        this.economies.put(name, getEconomy(name).add(amount));
    }

    @Override
    public void withdraw(String name, BigDecimal amount) {
        this.economies.put(name, getEconomy(name).subtract(amount));
    }

    @Override
    public void set(String name, BigDecimal amount) {
        this.economies.put(name, amount);
    }
}
