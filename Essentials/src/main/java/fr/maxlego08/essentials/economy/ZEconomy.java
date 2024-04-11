package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;

public class ZEconomy implements Economy {

    private final String name;
    private final String displayName;
    private final String symbol;
    private final String format;
    private final boolean isVaultEconomy;
    private final BigDecimal minValue;
    private final BigDecimal maxValue;

    public ZEconomy(ConfigurationSection section, String name) {
        this.name = name;
        this.displayName = section.getString("displayName", "default-money");
        this.symbol = section.getString("symbol", "$");
        this.format = section.getString("format", "%price%$");
        this.isVaultEconomy = section.getBoolean("vault", false);
        this.minValue = new BigDecimal(section.getString("min", "0"));
        this.maxValue = new BigDecimal(section.getString("max", "999999999999999"));
    }

    @Override
    public String toString() {
        return "ZEconomy{" + "name='" + name + '\'' + ", displayName='" + displayName + '\'' + ", symbol='" + symbol + '\'' + ", format='" + format + '\'' + ", isVaultEconomy=" + isVaultEconomy + '}';
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public String getFormat() {
        return this.format;
    }

    @Override
    public boolean isVaultEconomy() {
        return this.isVaultEconomy;
    }

    @Override
    public BigDecimal getMinValue() {
        return this.minValue;
    }

    @Override
    public BigDecimal getMaxValue() {
        return this.maxValue;
    }
}
