package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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

        System.out.println(economies);
    }

    @Override
    public boolean hasMoney(OfflinePlayer player, Economy economy) {
        return false;
    }

    @Override
    public BigDecimal getBalance(OfflinePlayer player, Economy economy) {
        return null;
    }

    private void perform(UUID uniqueId, Consumer<User> consumer) {
        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(uniqueId);

        if (user == null) { // Need to load the user, use async scheduler

            this.plugin.getScheduler().runAsync(wrappedTask -> iStorage.updateUserMoney(uniqueId, consumer));
        } else {
            
            consumer.accept(user);
        }
    }

    @Override
    public boolean deposit(UUID uniqueId, Economy economy, BigDecimal amount) {
        perform(uniqueId, user -> user.deposit(economy, amount));
        return true;
    }

    @Override
    public boolean withdraw(UUID uniqueId, Economy economy, BigDecimal amount) {
        perform(uniqueId, user -> user.withdraw(economy, amount));
        return true;
    }

    @Override
    public boolean set(UUID uniqueId, Economy economy, BigDecimal amount) {
        perform(uniqueId, user -> user.set(economy, amount));
        return true;
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
        // Rework for more configuration about that
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(amount);
    }
}
