package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyProvider;
import fr.maxlego08.essentials.api.economy.NumberFormatReduction;
import fr.maxlego08.essentials.api.economy.NumberMultiplicationFormat;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final List<NumberMultiplicationFormat> numberFormatSellMultiplication = new ArrayList<>();
    private String defaultEconomy;
    private BigDecimal minimumPayAmount;
    private PriceFormat priceFormat;
    private List<NumberFormatReduction> priceReductions;
    private String priceDecimalFormat;
    private DecimalFormat decimalFormat;

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

        this.loadInventory("confirm_pay_inventory");
        this.decimalFormat = new DecimalFormat(this.priceDecimalFormat);
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
    public Economy getVaultEconomy() {
        return this.economies.stream().filter(Economy::isVaultEconomy).findFirst().orElseGet(this::getDefaultEconomy);
    }

    @Override
    public String format(Number number) {
        return this.format(this.priceFormat, number);
    }

    @Override
    public String format(PriceFormat priceFormat, Number number) {
        return switch (priceFormat) {
            case PRICE_WITH_REDUCTION -> getDisplayBalance(number);
            case PRICE_WITH_DECIMAL_FORMAT -> decimalFormat.format(number);
            default -> number.toString();
        };
    }

    @Override
    public String format(Economy economy, Number number) {
        return economy.format(format(economy.getPriceFormat(), number), number.longValue());
    }

    protected String getDisplayBalance(Number number) {
        BigDecimal numValue = (number instanceof BigDecimal) ? (BigDecimal) number : BigDecimal.valueOf(number.longValue());

        for (NumberFormatReduction config : this.priceReductions) {
            if (numValue.compareTo(config.maxAmount()) < 0) {
                String displayText = config.display();
                if (config.format().isEmpty()) {
                    return displayText.replace("%amount%", numValue.toString());
                }
                BigDecimal divisor = config.maxAmount().equals(BigDecimal.valueOf(1000)) ? BigDecimal.valueOf(1000.0) : config.maxAmount().divide(BigDecimal.valueOf(1000.0), 2, RoundingMode.HALF_UP);
                String formattedAmount = String.format(config.format(), numValue.divide(divisor, 2, RoundingMode.HALF_UP));
                return displayText.replace("%amount%", formattedAmount);
            }
        }
        return numValue.toString();
    }


    @Override
    public List<NumberMultiplicationFormat> getNumberFormatSellMultiplication() {
        return this.numberFormatSellMultiplication;
    }

    @Override
    public Optional<NumberMultiplicationFormat> getMultiplication(String format) {
        return this.numberFormatSellMultiplication.stream().filter(numberMultiplicationFormat -> numberMultiplicationFormat.format().equalsIgnoreCase(format)).findFirst();
    }

    @Override
    public void pay(UUID fromUuid, String fromName, UUID toUuid, String toName, Economy economy, BigDecimal amount) {

        perform(fromUuid, user -> user.withdraw(toUuid, economy, amount));
        perform(toUuid, user -> user.deposit(fromUuid, economy, amount));

        message(fromUuid, Message.COMMAND_PAY_SENDER, "%amount%", this.format(economy, amount), "%player%", toName);
        message(toUuid, Message.COMMAND_PAY_RECEIVER, "%amount%", this.format(economy, amount), "%player%", fromName);
    }

    @Override
    public PriceFormat getPriceFormat() {
        return this.priceFormat;
    }

    @Override
    public List<NumberFormatReduction> getPriceReductions() {
        return this.priceReductions;
    }

    @Override
    public String getPriceDecimalFormat() {
        return this.priceDecimalFormat;
    }
}
