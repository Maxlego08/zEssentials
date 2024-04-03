package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;

public class ZEconomy implements Economy {

    private final String name;
    private final String displayName;
    private final String currency;
    private final String format;
    private final boolean isVaultEconomy;

    public ZEconomy(String name, String displayName, String currency, String format, boolean isVaultEconomy) {
        this.name = name;
        this.displayName = displayName;
        this.currency = currency;
        this.format = format;
        this.isVaultEconomy = isVaultEconomy;
    }

    public ZEconomy(ConfigurationSection section, String name) {
        this.name = name;
        this.displayName = section.getString("displayName");
        this.currency = section.getString("currecny");
        this.format = section.getString("format");
        this.isVaultEconomy = section.getBoolean("vault", false);
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
    public String getCurrency() {
        return null;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public boolean isVaultEconomy() {
        return this.isVaultEconomy;
    }
}
