package fr.maxlego08.essentials.api.economy;

public interface Economy {

    String getName();

    String getDisplayName();

    String getCurrency();

    String getFormat();

    boolean isVaultEconomy();

    default String format(String priceAsString, long amount) {
        return getCurrency().replace("%price%", priceAsString).replace("%s%", amount > 1 ? "s" : "");
    }

}
