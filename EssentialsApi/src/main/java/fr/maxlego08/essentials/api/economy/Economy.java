package fr.maxlego08.essentials.api.economy;

import java.math.BigDecimal;

public interface Economy {

    String getName();

    String getDisplayName();

    String getSymbol();

    String getFormat();

    boolean isVaultEconomy();

    BigDecimal getMinValue();

    BigDecimal getMaxValue();

    BigDecimal getMinPayValue();

    BigDecimal getMaxPayValue();

    BigDecimal getMinConfirmInventory();

    boolean isEnablePay();

    boolean isEnableConfirmInventory();

    default String format(String priceAsString, long amount) {
        return getFormat().replace("%price%", priceAsString).replace("%s%", amount > 1 ? "s" : "");
    }

}
