package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import org.bukkit.configuration.ConfigurationSection;

import java.math.BigDecimal;
import java.util.Map;

public class ZEconomy implements Economy {

    private final String name;
    private final String displayName;
    private final String symbol;
    private final String format;
    private final boolean isVaultEconomy;
    private final BigDecimal minValue;
    private final BigDecimal maxValue;
    private final BigDecimal minPayValue;
    private final BigDecimal maxPayValue;
    private final boolean isEnablePay;
    private final boolean isEnableConfirmInventory;
    private final BigDecimal minConfirmInventory;
    private final PriceFormat priceFormat;

    public ZEconomy(TypedMapAccessor typedMapAccessor, String name) {
        this.name = name;
        this.displayName = typedMapAccessor.getString("display-name", "default-money");
        this.symbol = typedMapAccessor.getString("symbol", "$");
        this.format = typedMapAccessor.getString("format", "%price%$");
        this.isVaultEconomy = typedMapAccessor.getBoolean("vault", false);
        this.minValue = new BigDecimal(typedMapAccessor.getString("min", "0"));
        this.maxValue = new BigDecimal(typedMapAccessor.getString("max", "999999999999999"));
        this.minPayValue = new BigDecimal(typedMapAccessor.getString("min-pay", "0.1"));
        this.maxPayValue = new BigDecimal(typedMapAccessor.getString("max-pay", "999999999999999"));
        this.isEnablePay = typedMapAccessor.getBoolean("enable-pay", true);
        this.isEnableConfirmInventory = typedMapAccessor.getBoolean("enable-confirm-inventory", false);
        this.minConfirmInventory = new BigDecimal(typedMapAccessor.getString("min-confirm-inventory", "0"));
        this.priceFormat = PriceFormat.valueOf(typedMapAccessor.getString("price-format", "PRICE_RAW").toUpperCase());
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

    @Override
    public BigDecimal getMinPayValue() {
        return this.minPayValue;
    }

    @Override
    public BigDecimal getMaxPayValue() {
        return this.maxPayValue;
    }

    @Override
    public BigDecimal getMinConfirmInventory() {
        return this.minConfirmInventory;
    }

    @Override
    public boolean isPaymentEnabled() {
        return this.isEnablePay;
    }

    @Override
    public boolean isConfirmInventoryEnabled() {
        return this.isEnableConfirmInventory;
    }

    @Override
    public PriceFormat getPriceFormat() {
        return this.priceFormat;
    }
}
