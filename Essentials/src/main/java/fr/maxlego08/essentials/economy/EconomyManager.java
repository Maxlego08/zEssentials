package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EconomyManager extends ZModule implements EconomyProvider {

    private final List<Economy> economies = new ArrayList<>();
    private String defaultEconomy;

    public EconomyManager(ZEssentialsPlugin plugin) {
        super(plugin, "economy");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.economies.clear();

        YamlConfiguration configuration = getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("economies");
        if (configurationSection == null) return;

        configurationSection.getKeys(false).forEach(economyName -> {

            ConfigurationSection section = configurationSection.getConfigurationSection(economyName);
            if (section == null) return;

            this.economies.add(new ZEconomy(section, economyName));
            this.plugin.getLogger().info("Create economy " + economyName + " !");
        });
    }

    @Override
    public boolean hasMoney(OfflinePlayer player, Economy economy) {
        return false;
    }

    @Override
    public BigDecimal getBalance(OfflinePlayer player, Economy economy) {
        return null;
    }

    @Override
    public boolean deposit(OfflinePlayer player, Economy economy, BigDecimal amount) {
        return false;
    }

    @Override
    public boolean withdraw(OfflinePlayer player, Economy economy, BigDecimal amount) {
        return false;
    }

    @Override
    public boolean set(OfflinePlayer player, Economy economy, BigDecimal amount) {
        return EconomyProvider.super.set(player, economy, amount);
    }

    @Override
    public Collection<Economy> getEconomies() {
        return Collections.unmodifiableCollection(this.economies);
    }

    @Override
    public Optional<Economy> getEconomy(String economyName) {
        return this.economies.stream().filter(economy -> economy.getName().equalsIgnoreCase(economyName)).findFirst();
    }

    @Override
    public Economy getDefaultEconomy() {
        return getEconomy(this.defaultEconomy).orElse(null);
    }

    @Override
    public String format(double amount) {
        return String.valueOf(amount);
    }
}
