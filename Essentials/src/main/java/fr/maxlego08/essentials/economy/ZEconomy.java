package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;

public class ZEconomy implements Economy {

    private final String name;
    private final String displayName;
    private final String symbol;
    private final String format;
    private final boolean isVaultEconomy;

    public ZEconomy(String name, String displayName, String currency, String format, boolean isVaultEconomy) {
        this.name = name;
        this.displayName = displayName;
        this.symbol = currency;
        this.format = format;
        this.isVaultEconomy = isVaultEconomy;
    }

    public ZEconomy(ConfigurationSection section, String name) {
        this.name = name;
        this.displayName = section.getString("displayName", "default-money");
        this.symbol = section.getString("symbol", "$");
        this.format = section.getString("format", "%price%$");
        this.isVaultEconomy = section.getBoolean("vault", false);
    }

    @Override
    public String toString() {
        return "ZEconomy{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", format='" + format + '\'' +
                ", isVaultEconomy=" + isVaultEconomy +
                '}';
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
}
