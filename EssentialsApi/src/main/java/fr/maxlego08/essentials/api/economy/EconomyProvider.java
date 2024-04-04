package fr.maxlego08.essentials.api.economy;

import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface EconomyProvider extends Module {

    boolean hasMoney(OfflinePlayer player, Economy economy);

    BigDecimal getBalance(OfflinePlayer player, Economy economy);

    boolean deposit(OfflinePlayer player, Economy economy, BigDecimal amount);

    boolean withdraw(OfflinePlayer player, Economy economy, BigDecimal amount);

    boolean set(OfflinePlayer player, Economy economy, BigDecimal amount);

    Collection<Economy> getEconomies();

    Optional<Economy> getEconomy(String economyName);

    Economy getDefaultEconomy();

    String format(double amount);
}
