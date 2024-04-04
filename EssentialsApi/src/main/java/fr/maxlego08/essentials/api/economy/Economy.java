package fr.maxlego08.essentials.api.economy;

public interface Economy {

    String getName();

    String getDisplayName();

    String getSymbol();

    String getFormat();

    boolean isVaultEconomy();

    default String format(String priceAsString, long amount) {
        return getFormat().replace("%price%", priceAsString).replace("%s%", amount > 1 ? "s" : "");
    }

}
