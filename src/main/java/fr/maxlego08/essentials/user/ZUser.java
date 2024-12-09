package fr.maxlego08.essentials.user;

import com.tcoded.folialib.impl.PlatformScheduler;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.CooldownDTO;
import fr.maxlego08.essentials.api.dto.EconomyDTO;
import fr.maxlego08.essentials.api.dto.HomeDTO;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.dto.OptionDTO;
import fr.maxlego08.essentials.api.dto.SanctionDTO;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.dto.VoteSiteDTO;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.event.events.user.UserEconomyPostUpdateEvent;
import fr.maxlego08.essentials.api.event.events.user.UserEconomyUpdateEvent;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.PrivateMessage;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.utils.DynamicCooldown;
import fr.maxlego08.essentials.api.worldedit.Selection;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.economy.EconomyModule;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ZUser extends ZUtils implements User {

    private final EssentialsPlugin plugin;
    private final Map<UUID, TeleportRequest> teleports = new HashMap<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final UUID uniqueId;
    private final Map<Option, Boolean> options = new HashMap<>();
    private final Map<String, BigDecimal> balances = new HashMap<>();
    private final List<Home> homes = new ArrayList<>();
    private final List<MailBoxItem> mailBoxItems = new ArrayList<>();
    private final DynamicCooldown dynamicCooldown = new DynamicCooldown();
    private final Selection selection = new ZSelection();
    private WorldEditTask worldEditTask;
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
    private long playTime;
    private long currentSessionPlayTime;
    private String address;
    private Kit previewKit;
    private Map<Material, String> powerTools = new HashMap<>();
    private long vote;
    private long offlineVote;
    private Map<String, Long> lastVotes = new HashMap<>();
    private Home currentDeleteHome;
    private long flySeconds;

    private boolean freeze;

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


        var request = targetUser.getTeleportRequest();
        if (request == null) {
            message(this, Message.COMMAND_TP_CANCEL_ERROR, targetUser);
            return;
        }

        if (!request.isValid()) {
            message(this, Message.COMMAND_TP_CANCEL_ERROR, targetUser);
            this.teleports.remove(targetUser.getUniqueId());
            return;
        }

        if (request.getFromUser() == this) {

            targetUser.setTeleportRequest(null);
            this.teleports.remove(targetUser.getUniqueId());

            message(this, Message.COMMAND_TP_CANCEL_SENDER, targetUser);
            message(targetUser, Message.COMMAND_TP_CANCEL_RECEIVER, this);
        } else {

            message(this, Message.COMMAND_TP_CANCEL_ERROR, targetUser);
        }
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

        if (teleportationModule.isTeleportDelayBypass() && this.hasPermission(Permission.ESSENTIALS_TELEPORT_BYPASS) || atomicInteger.get() <= 0) {
            this.teleport(teleportationModule, location, successMessage, args);
            return;
        }

        PlatformScheduler platformScheduler = this.plugin.getScheduler();
        platformScheduler.runAtLocationTimer(location, wrappedTask -> {

            if (!this.isOnline()) {
                wrappedTask.cancel();
                return;
            }

            if (!same(playerLocation, getPlayer().getLocation())) {

                message(this, Message.TELEPORT_MOVE);
                wrappedTask.cancel();
                return;
            }

            int currentSecond = atomicInteger.getAndDecrement();
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
    public void setCooldownSilent(String key, long expiredAt) {
        this.cooldowns.put(key, expiredAt);
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
        return getBalance(economy).compareTo(bigDecimal) > 0;
    }

    @Override
    public void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason) {
        Economy finalEconomy;
        BigDecimal finalBigDecimal;

        if (isOnline() && this.plugin.getServer().isPrimaryThread()) {
            UserEconomyUpdateEvent event = new UserEconomyUpdateEvent(this, economy, bigDecimal);
            event.callEvent();

            if (event.isCancelled()) return;

            finalEconomy = event.getEconomy();
            finalBigDecimal = event.getAmount();
        } else {

            finalBigDecimal = bigDecimal;
            finalEconomy = economy;
        }

        BigDecimal fromAmount = this.balances.getOrDefault(finalEconomy.getName(), BigDecimal.ZERO);
        BigDecimal toAmount = (finalBigDecimal.compareTo(finalEconomy.getMinValue()) < 0) ? finalEconomy.getMinValue() : (finalBigDecimal.compareTo(finalEconomy.getMaxValue()) > 0) ? finalEconomy.getMaxValue() : finalBigDecimal;
        this.balances.put(finalEconomy.getName(), toAmount);

        getStorage().updateEconomy(this.uniqueId, finalEconomy, finalBigDecimal);
        getStorage().storeTransactions(fromUuid, this.uniqueId, finalEconomy, fromAmount, toAmount, reason);

        if (isOnline() && this.plugin.getServer().isPrimaryThread()) {
            UserEconomyPostUpdateEvent postUpdateEvent = new UserEconomyPostUpdateEvent(this, finalEconomy, finalBigDecimal);
            postUpdateEvent.callEvent(this.plugin);
        }
    }

    @Override
    public void set(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {
        set(fromUuid, economy, bigDecimal, EconomyModule.NO_REASON);
    }

    @Override
    public void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason) {
        set(fromUuid, economy, getBalance(economy).subtract(bigDecimal), reason);
    }

    @Override
    public void withdraw(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {
        set(fromUuid, economy, getBalance(economy).subtract(bigDecimal));
    }

    @Override
    public void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal, String reason) {
        set(fromUuid, economy, getBalance(economy).add(bigDecimal), reason);
    }

    @Override
    public void deposit(UUID fromUuid, Economy economy, BigDecimal bigDecimal) {
        set(fromUuid, economy, getBalance(economy).add(bigDecimal));
    }

    @Override
    public void set(Economy economy, BigDecimal bigDecimal) {
        set(this.plugin.getConsoleUniqueId(), economy, bigDecimal);
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
    public void set(Economy economy, BigDecimal bigDecimal, String reason) {
        set(this.plugin.getConsoleUniqueId(), economy, bigDecimal, reason);
    }

    @Override
    public void deposit(Economy economy, BigDecimal bigDecimal, String reason) {
        deposit(this.plugin.getConsoleUniqueId(), economy, bigDecimal, reason);
    }

    @Override
    public void withdraw(Economy economy, BigDecimal bigDecimal, String reason) {
        withdraw(this.plugin.getConsoleUniqueId(), economy, bigDecimal, reason);
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
    public boolean setHome(String name, Location location, boolean force) {

        if (!force && isHomeName(name)) {
            message(this, Message.COMMAND_SET_HOME_CREATE_CONFIRM, "%name%", name);
            return false;
        }

        AtomicReference<Material> material = new AtomicReference<>(null);
        getHome(name).ifPresent(home -> material.set(home.getMaterial()));

        // Delete home with the same name before
        this.homes.removeIf(home -> home.getName().equalsIgnoreCase(name));

        Home home = new ZHome(location, name, material.get());
        this.homes.add(home);
        this.getStorage().upsertHome(this.uniqueId, home);

        return true;
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
        this.homes.addAll(homeDTOS.stream().map(homeDTO -> {
            try {
                return new ZHome(stringAsLocation(homeDTO.location()), homeDTO.name(), homeDTO.material() == null ? null : Material.valueOf(homeDTO.material()));
            } catch (Exception exception) {
                plugin.getLogger().severe("Impossible to load the home " + homeDTO.name() + " for " + this.name + " Debug: " + homeDTO);
                exception.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).toList());
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

    @Override
    public long getPlayTime() {
        return this.playTime + ((System.currentTimeMillis() - this.currentSessionPlayTime) / 1000);
    }

    @Override
    public void setPlayTime(long playtime) {
        this.playTime = playtime;
    }

    @Override
    public long getCurrentSessionPlayTime() {
        return this.currentSessionPlayTime;
    }

    @Override
    public void startCurrentSessionPlayTime() {
        this.currentSessionPlayTime = System.currentTimeMillis();
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public long getKitCooldown(Kit kit) {
        return this.getCooldown("kit:" + kit.getName());
    }

    @Override
    public boolean isKitCooldown(Kit kit) {
        return isCooldown("kit:" + kit.getName());
    }

    @Override
    public void addKitCooldown(Kit kit, long cooldown) {
        this.addCooldown("kit:" + kit.getName(), cooldown);
    }

    @Override
    public void openKitPreview(Kit kit) {
        this.previewKit = kit;
        this.plugin.openInventory(getPlayer(), "kit_preview");
    }

    @Override
    public void removeCooldown(String cooldownName) {
        this.cooldowns.remove(cooldownName);
    }

    @Override
    public void setPowerTools(Material type, String command) {
        this.powerTools.put(type, command);
        this.getStorage().setPowerTools(this.uniqueId, type, command);
    }

    @Override
    public Map<Material, String> getPowerTools() {
        return this.powerTools;
    }

    @Override
    public void setPowerTools(Map<Material, String> powerTools) {
        this.powerTools = powerTools;
    }

    @Override
    public Optional<String> getPowerTool(Material material) {
        return Optional.ofNullable(this.powerTools.get(material));
    }

    @Override
    public Kit getKitPreview() {
        return this.previewKit;
    }

    @Override
    public void deletePowerTools(Material material) {
        this.powerTools.remove(material);
        this.getStorage().deletePowerTools(this.uniqueId, material);
    }

    @Override
    public List<MailBoxItem> getMailBoxItems() {
        return this.mailBoxItems;
    }

    @Override
    public void setMailBoxItems(List<MailBoxDTO> mailBoxItems) {
        this.mailBoxItems.clear();
        this.mailBoxItems.addAll(mailBoxItems.stream().map(MailBoxItem::new).toList());
    }

    @Override
    public void addMailBoxItem(MailBoxItem mailBoxItem) {
        this.mailBoxItems.add(mailBoxItem);
        this.getStorage().addMailBoxItem(mailBoxItem);
    }

    @Override
    public DynamicCooldown getDynamicCooldown() {
        return dynamicCooldown;
    }

    @Override
    public long getVote() {
        return vote;
    }

    @Override
    public void setVote(long amount) {
        this.vote = amount;
        this.getStorage().setVote(this.uniqueId, this.vote, -1);
    }

    @Override
    public void addVote(long amount) {
        this.setVote(this.vote + amount);
    }

    @Override
    public void removeVote(long amount) {
        this.setVote(this.vote - amount);
    }

    @Override
    public void setWithDTO(UserDTO userDTO) {
        this.vote = userDTO.vote();
        this.offlineVote = userDTO.vote_offline();
        this.playTime = userDTO.play_time();
        this.lastLocation = stringAsLocation(userDTO.last_location());
        this.freeze = userDTO.frozen() != null && userDTO.frozen();
        this.flySeconds = userDTO.fly_seconds();
    }

    @Override
    public void setVoteSite(String site) {
        long ms = System.currentTimeMillis();

        if (this.lastVotes.containsKey(site) && ms - this.lastVotes.get(site) < 500) return;

        this.lastVotes.put(site, ms);
        this.getStorage().setLastVote(this.uniqueId, site);
    }

    @Override
    public long getLastVoteSite(String site) {
        return this.lastVotes.getOrDefault(site, 0L);
    }

    @Override
    public long getOfflineVotes() {
        return this.offlineVote;
    }

    @Override
    public void setVoteSites(List<VoteSiteDTO> select) {
        this.lastVotes = select.stream().collect(Collectors.toMap(VoteSiteDTO::site, value -> value.last_vote_at().getTime()));
    }

    @Override
    public void resetOfflineVote() {
        this.getStorage().setVote(this.uniqueId, -1, 0);
    }

    @Override
    public Selection getSelection() {
        return this.selection;
    }

    @Override
    public boolean hasWorldeditTask() {
        return this.worldEditTask != null && this.worldEditTask.getWorldeditStatus().isRunning();
    }

    @Override
    public WorldEditTask getWorldeditTask() {
        return this.worldEditTask;
    }

    @Override
    public void setWorldeditTask(WorldEditTask worldEditTask) {
        this.worldEditTask = worldEditTask;
    }

    @Override
    public ItemStack getItemInMainHand() {
        return getPlayer().getInventory().getItemInMainHand();
    }

    @Override
    public void setItemInMainHand(ItemStack itemStack) {
        getPlayer().getInventory().setItemInMainHand(itemStack);
    }

    @Override
    public void playSound(Sound sound, float volume, float pitch) {
        var player = getPlayer();
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    @Override
    public boolean isFrozen() {
        return freeze;
    }

    @Override
    public void setFrozen(boolean b) {
        freeze = b;
    }

    @Override
    public Optional<Home> getCurrentDeleteHome() {
        return Optional.ofNullable(this.currentDeleteHome);
    }

    @Override
    public void setCurrentDeleteHome(Home currentDeleteHome) {
        this.currentDeleteHome = currentDeleteHome;
    }

    @Override
    public long getFlySeconds() {
        return this.flySeconds;
    }

    @Override
    public void setFlySeconds(long seconds) {
        this.flySeconds = seconds;
        getStorage().upsertFlySeconds(this.uniqueId, this.flySeconds);
    }

    @Override
    public void addFlySeconds(long seconds) {
        this.flySeconds += seconds;
        getStorage().upsertFlySeconds(this.uniqueId, this.flySeconds);
    }

    @Override
    public void removeFlySeconds(long seconds) {
        this.flySeconds -= seconds;
        getStorage().upsertFlySeconds(this.uniqueId, this.flySeconds);
    }
}
