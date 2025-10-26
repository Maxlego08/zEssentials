package fr.maxlego08.essentials.module.modules.economy;

import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.dto.UserEconomyRankingDTO;
import fr.maxlego08.essentials.api.economy.Baltop;
import fr.maxlego08.essentials.api.economy.BaltopDisplay;
import fr.maxlego08.essentials.api.economy.DefaultEconomyConfiguration;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.EconomyManager;
import fr.maxlego08.essentials.api.economy.NumberFormatReduction;
import fr.maxlego08.essentials.api.economy.NumberMultiplicationFormat;
import fr.maxlego08.essentials.api.economy.OfflineEconomy;
import fr.maxlego08.essentials.api.economy.PriceFormat;
import fr.maxlego08.essentials.api.economy.UserBaltop;
import fr.maxlego08.essentials.api.event.events.economy.EconomyBaltopUpdateEvent;
import fr.maxlego08.essentials.api.event.events.user.UserFirstJoinEvent;
import fr.maxlego08.essentials.api.event.events.user.UserJoinEvent;
import fr.maxlego08.essentials.api.event.events.user.UserQuitEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.engine.Pagination;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import org.bukkit.OfflinePlayer;
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
    public static final String NO_REASON = "No reason";
    @NonLoadable
    private final List<Economy> economies = new ArrayList<>();
    private final List<NumberMultiplicationFormat> numberFormatSellMultiplication = new ArrayList<>();
    private final Map<Economy, Baltop> baltops = new HashMap<>();
    private final Map<UUID, OfflineEconomy> offlinePlayers = new HashMap<>();
    private final List<DefaultEconomyConfiguration> defaultEconomies = new ArrayList<>();
    private final Map<UUID, List<Consumer<User>>> userRequestQueue = new HashMap<>();
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
    private String payWithdrawReason;
    private String payDepositReason;
    private String commandGiveReason;
    private String commandGiveAllReason;
    private String commandGiveRandomReason;
    private String commandTakeReason;
    private String commandResetReason;
    private String commandSetReason;
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
        PlatformScheduler serverImplementation = this.plugin.getScheduler();
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
    public boolean hasMoney(OfflinePlayer offlinePlayer, Economy economy, BigDecimal decimal) {
        BigDecimal bigDecimal = getBalance(offlinePlayer, economy);
        return bigDecimal.compareTo(decimal) > 0;
    }

    @Override
    public BigDecimal getBalance(OfflinePlayer player, Economy economy) {
        if (player.isOnline()) {
            User user = this.plugin.getUser(player.getUniqueId());
            if (user != null) return user.getBalance(economy);
        } else {
            OfflineEconomy offlineEconomy = getOfflineEconomy(player.getUniqueId());
            return offlineEconomy.getEconomy(economy.getName());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Runs the given consumer when the user with the given uuid is loaded.
     * If the user is already loaded, the consumer is run immediately.
     * If the user is not yet loaded, it is loaded asynchronously and the consumer is run as soon as it is loaded.
     * If multiple consumers are added for the same user, they are all run when the user is loaded.
     *
     * @param uniqueId the uuid of the user
     * @param consumer the consumer to run when the user is loaded
     */
    private void perform(UUID uniqueId, Consumer<User> consumer) {
        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(uniqueId);

        if (user != null) {
            consumer.accept(user);
            return;
        }

        synchronized (userRequestQueue) {
            if (userRequestQueue.containsKey(uniqueId)) {
                userRequestQueue.get(uniqueId).add(consumer);
                return;
            } else {
                List<Consumer<User>> consumers = new ArrayList<>();
                consumers.add(consumer);
                userRequestQueue.put(uniqueId, consumers);
            }
        }

        this.plugin.getScheduler().runAsync(wrappedTask -> {
            User loadedUser = iStorage.updateUserMoney(uniqueId);

            List<Consumer<User>> consumers;
            synchronized (userRequestQueue) {
                consumers = userRequestQueue.remove(uniqueId);
            }

            for (Consumer<User> queuedConsumer : consumers) {
                queuedConsumer.accept(loadedUser);
            }
        });
    }

    @Override
    public boolean deposit(UUID uniqueId, Economy economy, BigDecimal amount) {
        return deposit(uniqueId, economy, amount, NO_REASON);
    }

    @Override
    public boolean set(UUID uniqueId, Economy economy, BigDecimal amount) {
        return set(uniqueId, economy, amount, NO_REASON);
    }

    @Override
    public boolean withdraw(UUID uniqueId, Economy economy, BigDecimal amount) {
        return withdraw(uniqueId, economy, amount, NO_REASON);
    }

    @Override
    public boolean deposit(UUID uniqueId, Economy economy, BigDecimal amount, String reason) {
        perform(uniqueId, user -> {
            user.deposit(economy, amount, reason);
            var offlineEconomy = getOfflineEconomy(uniqueId);
            offlineEconomy.deposit(economy.getName(), amount);
        });
        return true;
    }

    @Override
    public boolean withdraw(UUID uniqueId, Economy economy, BigDecimal amount, String reason) {
        perform(uniqueId, user -> {
            user.withdraw(economy, amount, reason);
            var offlineEconomy = getOfflineEconomy(uniqueId);
            offlineEconomy.withdraw(economy.getName(), amount);
        });
        return true;
    }

    @Override
    public boolean set(UUID uniqueId, Economy economy, BigDecimal amount, String reason) {
        perform(uniqueId, user -> {
            user.set(economy, amount, reason);
            var offlineEconomy = getOfflineEconomy(uniqueId);
            offlineEconomy.set(economy.getName(), amount);
        });
        return true;
    }

    @Override
    public void resetAll(Economy economy, String reason) {
        BigDecimal amount = BigDecimal.ZERO;

        this.plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
            User user = this.plugin.getUser(onlinePlayer.getUniqueId());
            if (user != null) {
                user.set(economy, amount, reason);
            }
        });

        this.offlinePlayers.values().forEach(offlineEconomy -> offlineEconomy.set(economy.getName(), amount));

        this.plugin.getStorageManager().getStorage().resetEconomy(economy, amount);

        refreshBaltop(economy);
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
        var economy = this.economies.stream().filter(Economy::isVaultEconomy).findFirst().orElseGet(this::getDefaultEconomy);
        if (economy == null) {
            this.plugin.getLogger().severe("Impossible to find the default or vault economy ! Check your configuration plz !");
        }
        return economy;
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
        var result = economy.format(format(economy.getPriceFormat(), number), number.longValue());
        if (result.contains(":")) {
            result = this.plugin.getInventoryManager().getFontImage().replace(result);
        }
        return result;
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

        perform(fromUuid, user -> user.withdraw(toUuid, economy, amount, this.payWithdrawReason.replace("%player%", toName)));
        perform(toUuid, user -> user.deposit(fromUuid, economy, amount, this.payDepositReason.replace("%player%", fromName)));

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
    public void onFirstJoin(UserFirstJoinEvent event) {
        if (this.defaultEconomies.isEmpty()) return;
        var user = event.getUser();
        for (DefaultEconomyConfiguration defaultEconomyConfiguration : this.defaultEconomies) {
            getEconomy(defaultEconomyConfiguration.economy()).ifPresent(economy -> user.deposit(economy, defaultEconomyConfiguration.amount()));
        }
    }

    @EventHandler
    public void onQuit(UserQuitEvent event) {
        User user = event.getUser();
        OfflineEconomy offlineEconomy = new ZOfflineEconomy(user.getBalances());
        this.offlinePlayers.put(user.getUniqueId(), offlineEconomy);
    }

    @EventHandler
    public void onConnect(UserJoinEvent event) {
        this.offlinePlayers.remove(event.getUser().getUniqueId());
    }

    @Override
    public String getCommandGiveReason() {
        return commandGiveReason;
    }

    @Override
    public String getCommandGiveAllReason() {
        return commandGiveAllReason;
    }

    @Override
    public String getCommandGiveRandomReason() {
        return commandGiveRandomReason;
    }

    @Override
    public String getCommandTakeReason() {
        return commandTakeReason;
    }

    @Override
    public String getCommandResetReason() {
        return commandResetReason;
    }

    @Override
    public String getCommandSetReason() {
        return commandSetReason;
    }
}
