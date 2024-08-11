package fr.maxlego08.essentials.economy;

import com.tcoded.folialib.impl.ServerImplementation;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.economy.Baltop;
import fr.maxlego08.essentials.api.economy.BaltopDisplay;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.economy.NumberFormatReduction;
import fr.maxlego08.essentials.api.economy.NumberMultiplicationFormat;
import fr.maxlego08.essentials.api.economy.OfflineEconomy;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.essentials.api.economy.UserBaltop;
import fr.maxlego08.essentials.api.event.events.economy.EconomyBaltopUpdateEvent;
import fr.maxlego08.essentials.api.event.events.user.UserQuitEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EconomyModule extends ZModule implements EconomyManager {

    @NonLoadable
    private final List<Economy> economies = new ArrayList<>();
    private final List<NumberMultiplicationFormat> numberFormatSellMultiplication = new ArrayList<>();
    private final Map<Economy, Baltop> baltops = new HashMap<>();
    private final Map<UUID, OfflineEconomy> offlinePlayers = new HashMap<>();
    private String defaultEconomy;
    private BigDecimal minimumPayAmount;
    private PriceFormat priceFormat;
    private List<NumberFormatReduction> priceReductions;
    private String priceDecimalFormat;
    private DecimalFormat decimalFormat;
    private boolean enableBaltop;
    private boolean storeOfflinePlayerMoney;
    private long baltopRefreshSeconds;
    private String baltopPlaceholderUserEmpty;
    private String baltopMessageEconomy;
    private int baltopMessageAmount;
    private BaltopDisplay baltopDisplay;
    private WrappedTask baltopTask;

    public EconomyModule(ZEssentialsPlugin plugin) {
        super(plugin, "economy");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.economies.clear();
        this.baltops.clear();

        YamlConfiguration configuration = getConfiguration();
        var mapList = configuration.getMapList("economies");
        mapList.forEach(map -> {

            String economyName = (String) map.get("name");
            this.economies.add(new ZEconomy(new TypedMapAccessor((Map<String, Object>) map), economyName));
            this.plugin.getLogger().info("Create economy " + economyName + " !");

        });

        this.loadInventory("confirm_pay_inventory");
        this.loadInventory("baltop");
        this.decimalFormat = new DecimalFormat(this.priceDecimalFormat);

        if (this.enableBaltop) startBaltopTask();

        if (this.storeOfflinePlayerMoney) {
            var iStorage = this.plugin.getStorageManager().getStorage();
            iStorage.fetchOfflinePlayerEconomies(economies -> {
                Map<UUID, Map<String, BigDecimal>> values = new HashMap<>();
                economies.forEach(economy -> values.computeIfAbsent(economy.unique_id(), k -> new HashMap<>()).merge(economy.economy_name(), economy.amount(), BigDecimal::add));
                values.forEach((uuid, map) -> this.offlinePlayers.put(uuid, new ZOfflineEconomy(map)));
            });
        }
    }

    private void startBaltopTask() {

        if (this.baltopTask != null) this.baltopTask.cancel();

        this.baltopTask = this.plugin.getScheduler().runTimer(() -> this.economies.forEach(this::refreshBaltop), 2, this.baltopRefreshSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void refreshBaltop(Economy economy) {
        ServerImplementation serverImplementation = this.plugin.getScheduler();
        serverImplementation.runAsync(wrappedTask -> {

            IStorage iStorage = this.plugin.getStorageManager().getStorage();
            var rankings = iStorage.getEconomyRanking(economy);

            Map<UUID, Long> userPositions = new HashMap<>();
            List<UserBaltop> userBaltops = new ArrayList<>();

            long position = 1;
            for (UserEconomyRankingDTO ranking : rankings) {
                long currentPosition = position++;
                userPositions.put(ranking.unique_id(), currentPosition);
                userBaltops.add(new ZUserBaltop(ranking.unique_id(), ranking.name(), ranking.amount(), currentPosition));
            }

            Baltop baltop = new ZBaltop(economy, userBaltops, userPositions);
            this.baltops.put(economy, baltop);

            serverImplementation.runNextTick(wrappedTask1 -> new EconomyBaltopUpdateEvent(baltop).callEvent());
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
                String format = config.format();
                if (displayText != null) {
                    if (format.isEmpty() || format.contains("#")) {
                        return displayText.replace("%amount%", config.format().contains("#") ? new DecimalFormat(config.format()).format(numValue) : numValue.toString());
                    }

                    BigDecimal divisor = config.maxAmount().equals(BigDecimal.valueOf(1000)) ? BigDecimal.valueOf(1000.0) : config.maxAmount().divide(BigDecimal.valueOf(1000.0), 2, RoundingMode.HALF_UP);
                    String formattedAmount = String.format(config.format(), numValue.divide(divisor, 2, RoundingMode.HALF_UP));
                    return displayText.replace("%amount%", formattedAmount);
                } else {
                    plugin.getLogger().severe("Display text is null for " + format + " format ! config.yml of economy module");
                }
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

    @Override
    public String getBaltopPlaceholderUserEmpty() {
        return this.baltopPlaceholderUserEmpty;
    }

    @Override
    public Baltop getBaltop(Economy economy) {
        return this.baltops.get(economy);
    }

    @Override
    public Optional<UserBaltop> getPosition(String economyName, int position) {
        Optional<Economy> optional = getEconomy(economyName);
        if (optional.isPresent()) {
            Economy economy = optional.get();
            Baltop baltop = getBaltop(economy);
            if (baltop != null) {
                return baltop.getPosition(position);
            }
        }
        return Optional.empty();
    }

    @Override
    public long getUserPosition(String economyName, UUID uuid) {
        Optional<Economy> optional = getEconomy(economyName);
        if (optional.isPresent()) {
            Economy economy = optional.get();
            Baltop baltop = getBaltop(economy);
            if (baltop != null) {
                return baltop.getUserPosition(uuid);
            }
        }
        return -1;
    }

    @Override
    public void sendBaltop(Player player, int page) {

        if (this.baltopDisplay == BaltopDisplay.MESSAGE) {
            this.sendMessageBaltop(player, page);
        } else {
            this.plugin.openInventory(player, "baltop");
        }
    }

    private void sendMessageBaltop(Player player, int page) {

        Optional<Economy> optional = this.getEconomy(this.baltopMessageEconomy);
        if (optional.isEmpty()) {
            message(player, Message.COMMAND_BALTOP_ERROR, "%name%", this.baltopMessageEconomy);
            return;
        }

        Economy economy = optional.get();
        Baltop baltop = getBaltop(economy);
        List<UserBaltop> userBaltops = baltop.getUsers();

        Pagination<UserBaltop> pagination = new Pagination<>();
        int maxPage = getMaxPage(userBaltops, baltopMessageAmount);
        page = page > maxPage ? maxPage : Math.max(page, 1);

        message(player, Message.COMMAND_BALTOP_HEADER, "%page%", page, "%nextPage%", page + 1, "%previousPage%", page - 1, "%maxPage%", maxPage);

        for (UserBaltop userBaltop : pagination.paginate(userBaltops, baltopMessageAmount, page)) {
            message(player, Message.COMMAND_BALTOP, "%name%", userBaltop.getName(), "%uuid%", userBaltop.getUniqueId(), "%position%", userBaltop.getPosition(), "%amount%", format(economy, userBaltop.getAmount()));
        }
    }

    public Map<UUID, OfflineEconomy> getOfflinePlayers() {
        return offlinePlayers;
    }

    private OfflineEconomy getOfflineEconomy(UUID uniqueId) {
        return this.offlinePlayers.getOrDefault(uniqueId, new ZOfflineEconomy(new HashMap<>()));
    }

    @Override
    public BigDecimal getBalanceOffline(UUID uniqueId) {
        return getOfflineEconomy(uniqueId).getEconomy(this.defaultEconomy);
    }

    @EventHandler
    public void onQuit(UserQuitEvent event) {
        User user = event.getUser();
        OfflineEconomy offlineEconomy = new ZOfflineEconomy(user.getBalances());
        this.offlinePlayers.put(user.getUniqueId(), offlineEconomy);
    }
}
