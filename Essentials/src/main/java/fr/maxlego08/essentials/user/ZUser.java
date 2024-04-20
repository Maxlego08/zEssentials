package fr.maxlego08.essentials.user;

import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.database.dto.CooldownDTO;
import fr.maxlego08.essentials.api.database.dto.EconomyDTO;
import fr.maxlego08.essentials.api.database.dto.HomeDTO;
import fr.maxlego08.essentials.api.database.dto.OptionDTO;
import fr.maxlego08.essentials.api.database.dto.SanctionDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ZUser extends ZUtils implements User {

    private final EssentialsPlugin plugin;
    private final Map<UUID, TeleportRequest> teleports = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final UUID uniqueId;
    private final Map<Option, Boolean> options = new HashMap<>();
    private final Map<String, BigDecimal> balances = new HashMap<>();
    private final List<Home> homes = new ArrayList<>();
    private String name;
    private TeleportRequest teleportRequest;
    private User targetUser;
    private BigDecimal targetAmount;
    private Economy targetEconomy;
    private Location lastLocation;
    private boolean firstJoin;
    private int banId;
    private int muteId;
    private Sanction muteSanction;
    private Sanction banSanction;
    private List<Sanction> fakeSanctions;
    private String lastMessage;
    private PrivateMessage privateMessage;

    public ZUser(EssentialsPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    public static User fakeUser(EssentialsPlugin plugin, UUID uniqueId, String userName) {
        User user = new ZUser(plugin, uniqueId);
        user.setName(userName);
        return user;
    }

    private IStorage getStorage() {
        return this.plugin.getStorageManager().getStorage();
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getOfflinePlayer(this.uniqueId).isOnline();
    }

    @Override
    public boolean isIgnore(UUID uniqueId) {
        return false;
    }

    @Override
    public void sendTeleportRequest(User targetUser) {

        if (targetUser == null || !targetUser.isOnline()) {
            message(this, Message.COMMAND_TPA_ERROR_SAME);
            return;
        }

        if (targetUser.getUniqueId().equals(this.uniqueId)) {
            message(this, Message.COMMAND_TPA_ERROR_SAME);
            return;
        }

        if (targetUser.isIgnore(this.uniqueId)) {
            message(this, Message.COMMAND_TELEPORT_IGNORE_PLAYER, targetUser);
            return;
        }

        this.teleports.entrySet().removeIf(next -> !next.getValue().isValid());

        if (this.teleports.containsKey(targetUser.getUniqueId())) {
            message(this, Message.COMMAND_TPA_ERROR, targetUser);
            return;
        }

        TeleportationModule teleportationModule = this.plugin.getModuleManager().getModule(TeleportationModule.class);
        long expired = System.currentTimeMillis() + (teleportationModule.getTeleportTpaExpire() * 1000L);
        TeleportRequest teleportRequest = new ZTeleportRequest(this.plugin, targetUser, this, expired);
        targetUser.setTeleportRequest(teleportRequest);
        this.teleports.put(targetUser.getUniqueId(), teleportRequest);

        message(this, Message.COMMAND_TPA_SENDER, targetUser);
        message(targetUser, Message.COMMAND_TPA_RECEIVER, getPlayer());
    }

    @Override
    public void cancelTeleportRequest(User targetUser) {

        if (!this.teleports.containsKey(targetUser.getUniqueId())) {
            message(this, Message.COMMAND_TP_CANCEL_ERROR, targetUser);
            return;
        }

        this.teleports.remove(targetUser.getUniqueId());

        if (targetUser.getTeleportRequest() != null && targetUser.getTeleportRequest().getFromUser() == this) {
            targetUser.setTeleportRequest(null);
        }

        message(this, Message.COMMAND_TP_CANCEL_SENDER, targetUser);
        message(targetUser, Message.COMMAND_TP_CANCEL_RECEIVER, this);
    }

    @Override
    public Collection<TeleportRequest> getTeleportRequests() {
        return this.teleports.values();
    }

    @Override
    public TeleportRequest getTeleportRequest() {
        return teleportRequest;
    }

    @Override
    public void setTeleportRequest(TeleportRequest teleportRequest) {
        this.teleportRequest = teleportRequest;
    }

    @Override
    public void removeTeleportRequest(User user) {
        this.teleports.remove(user.getUniqueId());
    }

    @Override
    public void teleportNow(Location location) {
        // ToDo, https://github.com/PaperMC/Folia/?tab=readme-ov-file#current-broken-api
        // When folia API is update, remove this
        if (this.plugin.isFolia()) {
            this.setLastLocation();
        }
        this.plugin.getScheduler().teleportAsync(this.getPlayer(), location);
    }

    @Override
    public void teleport(Location location) {
        this.teleport(location, Message.TELEPORT_MESSAGE, Message.TELEPORT_SUCCESS);
    }

    @Override
    public void teleport(Location location, Message message, Message successMessage, Object... args) {

        TeleportationModule teleportationModule = this.plugin.getModuleManager().getModule(TeleportationModule.class);
        Location playerLocation = getPlayer().getLocation();
        AtomicInteger atomicInteger = new AtomicInteger(teleportationModule.getTeleportationDelay(getPlayer()));

        if (teleportationModule.isTeleportDelayBypass() && this.hasPermission(Permission.ESSENTIALS_TELEPORT_BYPASS)) {
            this.teleport(teleportationModule, location, successMessage, args);
            return;
        }

        ServerImplementation serverImplementation = this.plugin.getScheduler();
        serverImplementation.runAtLocationTimer(location, wrappedTask -> {

            if (!same(playerLocation, getPlayer().getLocation())) {

                message(this, Message.TELEPORT_MOVE);
                wrappedTask.cancel();
                return;
            }

            int currentSecond = atomicInteger.getAndDecrement();

            if (!this.isOnline()) {
                wrappedTask.cancel();
                return;
            }

            if (currentSecond == 0) {

                wrappedTask.cancel();
                this.teleport(teleportationModule, location, successMessage, args);
            } else {
                List<Object> objects = new ArrayList<>(Arrays.asList(args));
                objects.add("%seconds%");
                objects.add(currentSecond);

                message(this, message, objects.toArray());
            }

        }, 1, 20);

    }

    private void teleport(TeleportationModule teleportationModule, Location toLocation, Message message, Object... args) {
        Location location = getPlayer().isFlying() ? toLocation : teleportationModule.isTeleportSafety() ? toSafeLocation(toLocation) : toLocation;

        if (teleportationModule.isTeleportToCenter()) {
            location = location.getBlock().getLocation().add(0.5, 0, 0.5);
            location.setYaw(toLocation.getYaw());
            location.setPitch(toLocation.getPitch());
        }

        this.teleportNow(location);
        if (message != null) {
            message(this, message, args);
        }
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return getPlayer().hasPermission(permission.asPermission());
    }

    @Override
    public User getTargetUser() {
        return targetUser;
    }

    @Override
    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public boolean getOption(Option option) {
        return options.getOrDefault(option, false);
    }

    @Override
    public void setOption(Option option, boolean value) {
        this.options.put(option, value);
        this.getStorage().updateOption(this.uniqueId, option, value);
    }

    @Override
    public void setFakeOption(Option option, boolean value) {
        this.options.put(option, value);
    }

    @Override
    public Map<Option, Boolean> getOptions() {
        return this.options;
    }

    @Override
    public void setOptions(List<OptionDTO> options) {
        options.forEach((optionDTO) -> this.options.put(optionDTO.option_name(), optionDTO.option_value()));
    }

    @Override
    public Map<String, Long> getCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        return this.cooldowns;
    }

    @Override
    public void setCooldowns(List<CooldownDTO> cooldowns) {
        long currentTime = System.currentTimeMillis();
        cooldowns.stream().filter(cooldownDTO -> cooldownDTO.cooldown_value() > currentTime).forEach(cooldownDTO -> this.cooldowns.put(cooldownDTO.cooldown_name(), cooldownDTO.cooldown_value()));
    }

    @Override
    public void setCooldown(String key, long expiredAt) {
        this.cooldowns.put(key, expiredAt);
        this.getStorage().updateCooldown(this.uniqueId, key, expiredAt);
    }

    @Override
    public boolean isCooldown(String key) {
        return this.cooldowns.containsKey(key) && this.cooldowns.get(key) >= System.currentTimeMillis();
    }

    @Override
    public long getCooldown(String key) {
        return this.cooldowns.getOrDefault(key, 0L);
    }

    @Override
    public long getCooldownSeconds(String key) {
        long cooldown = getCooldown(key);
        return cooldown == 0 ? 0 : (cooldown - System.currentTimeMillis()) / 1000;
    }

    @Override
    public void addCooldown(String key, long seconds) {
        setCooldown(key, System.currentTimeMillis() + (1000L * seconds));
    }

    @Override
    public BigDecimal getBalance(Economy economy) {
        return this.balances.getOrDefault(economy.getName(), BigDecimal.ZERO);
    }

    @Override
    public boolean has(Economy economy, BigDecimal bigDecimal) {
        return bigDecimal.compareTo(getBalance(economy)) > 0;
    }

    @Override
    public void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {

        BigDecimal fromAmount = this.balances.getOrDefault(economy.getName(), BigDecimal.ZERO);
        BigDecimal toAmount = (bigDecimal.compareTo(economy.getMinValue()) < 0) ? economy.getMinValue() : (bigDecimal.compareTo(economy.getMaxValue()) > 0) ? economy.getMaxValue() : bigDecimal;
        this.balances.put(economy.getName(), toAmount);

        getStorage().updateEconomy(this.uniqueId, economy, bigDecimal);
        getStorage().storeTransactions(fromUuid, this.uniqueId, economy, fromAmount, toAmount);
    }

    @Override
    public void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {
        set(fromUuid, economy, getBalance(economy).subtract(bigDecimal));
    }

    @Override
    public void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {
        set(fromUuid, economy, getBalance(economy).add(bigDecimal));
    }

    @Override
    public void set(Economy economy, BigDecimal bigDecimal) {
        this.set(this.plugin.getConsoleUniqueId(), economy, bigDecimal);
    }

    @Override
    public void deposit(Economy economy, BigDecimal bigDecimal) {
        deposit(this.plugin.getConsoleUniqueId(), economy, bigDecimal);
    }

    @Override
    public void withdraw(Economy economy, BigDecimal bigDecimal) {
        withdraw(this.plugin.getConsoleUniqueId(), economy, bigDecimal);
    }

    @Override
    public Map<String, BigDecimal> getBalances() {
        return this.balances;
    }

    @Override
    public void setBalance(String key, BigDecimal value) {
        this.balances.put(key, value);
    }

    @Override
    public void setEconomies(List<EconomyDTO> economyDTOS) {
        economyDTOS.forEach(economyDTO -> this.balances.put(economyDTO.economy_name(), economyDTO.amount()));
    }

    @Override
    public void setTargetPay(User user, Economy economy, BigDecimal bigDecimal) {
        this.targetUser = user;
        this.targetEconomy = economy;
        this.targetAmount = bigDecimal;
    }

    @Override
    public @Nullable Economy getTargetEconomy() {
        return this.targetEconomy;
    }

    @Override
    public @Nullable BigDecimal getTargetDecimal() {
        return this.targetAmount;
    }

    @Override
    public void setLastLocation() {
        Player player = this.getPlayer();
        if (player == null) return;
        this.lastLocation = player.getLocation().clone();
        this.getStorage().upsertUser(this);
    }

    @Override
    public Location getLastLocation() {
        return this.lastLocation;
    }

    @Override
    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    @Override
    public boolean isFirstJoin() {
        return this.firstJoin;
    }

    @Override
    public void setFirstJoin() {
        this.firstJoin = true;
    }

    @Override
    public void setHome(String name, Location location) {
        // Delete home with the same name before
        removeHome(name);

        Home home = new ZHome(location, name, null);
        this.homes.add(home);
        this.getStorage().upsertHome(this.uniqueId, home);
    }

    @Override
    public Optional<Home> getHome(String name) {
        return this.homes.stream().filter(home -> home.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<Home> getHomes() {
        return this.homes;
    }

    @Override
    public void setHomes(List<HomeDTO> homeDTOS) {
        this.homes.addAll(homeDTOS.stream().map(homeDTO -> new ZHome(stringAsLocation(homeDTO.location()), homeDTO.name(), homeDTO.material() == null ? null : Material.valueOf(homeDTO.material()))).toList());
    }

    @Override
    public int countHomes() {
        return this.homes.size();
    }

    @Override
    public void removeHome(String name) {
        this.homes.removeIf(home -> home.getName().equalsIgnoreCase(name));
        this.getStorage().deleteHome(this.uniqueId, name);
    }

    @Override
    public boolean isHomeName(String homeName) {
        return getHome(homeName).isPresent();
    }

    @Override
    public int getActiveBanId() {
        return this.banId;
    }

    @Override
    public int getActiveMuteId() {
        return this.muteId;
    }

    @Override
    public void setSanction(Integer banId, Integer muteId) {
        this.banId = banId == null ? 0 : banId;
        this.muteId = muteId == null ? 0 : muteId;
    }

    @Override
    public Sanction getMuteSanction() {
        return this.muteSanction;
    }

    @Override
    public void setMuteSanction(Sanction sanction) {
        this.muteId = sanction == null ? 0 : sanction.getId();
        this.muteSanction = sanction;
    }

    @Override
    public boolean isMute() {
        return this.muteSanction != null && this.muteSanction.isActive();
    }

    @Override
    public List<Sanction> getFakeSanctions() {
        return this.fakeSanctions;
    }

    @Override
    public void setFakeSanctions(List<SanctionDTO> sanctions) {
        this.fakeSanctions = sanctions.stream().map(Sanction::fromDTO).toList();
    }

    @Override
    public Sanction getBanSanction() {
        return banSanction;
    }

    @Override
    public void setBanSanction(Sanction banSanction) {
        this.banId = banSanction != null ? banSanction.getId() : 0;
        this.banSanction = banSanction;
    }

    @Override
    public String getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public PrivateMessage setPrivateMessage(UUID uuid, String userName) {
        return this.privateMessage = new PrivateMessage(uuid, userName);
    }

    @Override
    public PrivateMessage getPrivateMessage() {
        return this.privateMessage;
    }

    @Override
    public boolean hasPrivateMessage() {
        return this.privateMessage != null;
    }
}
