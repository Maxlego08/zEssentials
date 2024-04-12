package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EconomyProvider extends Module {

    boolean hasMoney(OfflinePlayer player, Economy economy);

    BigDecimal getBalance(OfflinePlayer player, Economy economy);

    boolean deposit(UUID uniqueId, Economy economy, BigDecimal amount);

    boolean withdraw(UUID uniqueId, Economy economy, BigDecimal amount);

    boolean set(UUID uniqueId, Economy economy, BigDecimal amount);

    Collection<Economy> getEconomies();

    Optional<Economy> getEconomy(String economyName);

    Economy getDefaultEconomy();

    String format(Number number);

    List<NumberMultiplicationFormat> getNumberFormatSellMultiplication();

    Optional<NumberMultiplicationFormat> getMultiplication(String format);

    void pay(UUID fromUuid, String fromName, UUID toUuid, String toName, Economy economy, BigDecimal amount);

    PriceFormat getPriceFormat();

    List<NumberFormatReduction> getPriceReductions();

    String getPriceDecimalFormat();
}
