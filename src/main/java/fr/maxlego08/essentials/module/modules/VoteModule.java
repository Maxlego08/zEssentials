package fr.maxlego08.essentials.module.modules;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;
import fr.maxlego08.essentials.api.event.events.user.UserVoteEvent;
import fr.maxlego08.essentials.api.event.events.vote.VotePartyEvent;
import fr.maxlego08.essentials.api.event.events.vote.VotePartyStartEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.Key;
import fr.maxlego08.essentials.api.storage.StorageKey;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.vote.VoteCache;
import fr.maxlego08.essentials.api.vote.VoteManager;
import fr.maxlego08.essentials.api.vote.VotePartyReward;
import fr.maxlego08.essentials.api.vote.VoteResetConfiguration;
import fr.maxlego08.essentials.api.vote.VoteReward;
import fr.maxlego08.essentials.api.vote.VoteSiteConfiguration;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VoteModule extends ZModule implements VoteManager {

    @NonLoadable
    private final Key votePartyKey;
    @NonLoadable
    private final VoteCache voteCache = new VoteCache();
    @NonLoadable
    private long newVotePartyAmount;
    @NonLoadable
    private long newVotePartyUpdate;
    @NonLoadable
    private WrappedTask newVotePartyTask;
    @NonLoadable
    private long currentVoteAmount;
    private boolean enableVoteParty;
    private boolean enableVotePartyOpenVoteInventory;
    private boolean enableOfflineVoteMessage;
    private long votePartyObjective;
    @NonLoadable
    private VotePartyReward votePartyRewards;
    @NonLoadable
    private List<VoteReward> rewardsOnVote = new ArrayList<>();
    private List<VoteSiteConfiguration> sites;
    private String placeholderAvailable;
    private String placeholderCooldown;
    private VoteResetConfiguration resetConfiguration;

    public VoteModule(ZEssentialsPlugin plugin) {
        super(plugin, "vote");
        this.votePartyKey = new StorageKey(plugin, "vote-party-amount");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.currentVoteAmount = this.plugin.getServerStorage().getLong(this.votePartyKey);
        this.loadInventory("vote");

        var buttonManager = this.plugin.getButtonManager();
        var configuration = getConfiguration();
        var file = new File(getFolder(), "config.yml");

        var actions = buttonManager.loadActions((List<Map<String, Object>>) configuration.getList("vote-party-rewards.actions"), "vote-party-rewards.actions", file);
        var permissionActions = buttonManager.loadActions((List<Map<String, Object>>) configuration.getList("vote-party-rewards.permission-actions"), "vote-party-rewards.permission-actions", file);
        String permission = configuration.getString("vote-party-rewards.permission");
        List<String> globalCommands = configuration.getStringList("vote-party-rewards.global-commands");

        this.votePartyRewards = new VotePartyReward(actions, permission, permissionActions, globalCommands);

        this.rewardsOnVote.clear();
        for (Map<?, ?> map : configuration.getMapList("rewards-on-vote")) {
            TypedMapAccessor typedMapAccessor = new TypedMapAccessor((Map<String, Object>) map);
            int min = typedMapAccessor.getInt("min");
            int max = typedMapAccessor.getInt("max", Integer.MAX_VALUE);
            var rewardActions = typedMapAccessor.getStringList("commands");
            this.rewardsOnVote.add(new VoteReward(min, max, rewardActions));
        }
        
        this.placeholderAvailable = configuration.getString("placeholders.available", "&aAvailable");
        this.placeholderCooldown = configuration.getString("placeholders.cooldown", "&cCooldown");
        
        this.enableVoteParty = configuration.getBoolean("vote-party.enable");
        this.votePartyObjective = configuration.getLong("vote-party.objective");
        this.enableVotePartyOpenVoteInventory = configuration.getBoolean("vote-party.open-inventory");
        this.enableOfflineVoteMessage = configuration.getBoolean("enable-offline-vote-message");
        
        this.sites = new ArrayList<>();
        for (Map<?, ?> map : configuration.getMapList("vote-sites")) {
            TypedMapAccessor typedMapAccessor = new TypedMapAccessor((Map<String, Object>) map);
            String name = typedMapAccessor.getString("name");
            String url = typedMapAccessor.getString("url");
            long delay = typedMapAccessor.getLong("delay");
            this.sites.add(new VoteSiteConfiguration(name, url, delay));
        }

        this.resetConfiguration = new VoteResetConfiguration(
                configuration.getInt("reset-votes.day"),
                configuration.getInt("reset-votes.hour"),
                configuration.getInt("reset-votes.minute"),
                configuration.getInt("reset-votes.second")
        );
    }

    @Override
    public long getCurrentVotePartyAmount() {
        return this.currentVoteAmount;
    }

    @Override
    public void setCurrentVotePartyAmount(long amount) {
        this.currentVoteAmount = amount;
        this.plugin.getServerStorage().set(this.votePartyKey, this.currentVoteAmount);
    }

    @Override
    public void addCurrentVotePartyAmount(long amount) {
        this.setCurrentVotePartyAmount(this.currentVoteAmount + amount);
    }

    @Override
    public void removeCurrentVotePartyAmount(long amount) {
        this.setCurrentVotePartyAmount(this.currentVoteAmount - amount);
    }

    @Override
    public void onPlayerVote(UUID uniqueId, String site) {
        this.plugin.getScheduler().runNextTick(wrappedTask -> {

            UserVoteEvent event = new UserVoteEvent(uniqueId, site);
            event.callEvent();

            if (event.isCancelled()) return;

            boolean isOnline = Bukkit.getPlayer(uniqueId) != null;
            voteCache.addVote(uniqueId, isOnline);

            if (!this.voteCache.hasTask(uniqueId)) {
                scheduleDatabaseUpdateForPlayer(uniqueId);
            }

            var storage = this.plugin.getStorageManager().getStorage();
            User user = this.plugin.getUser(uniqueId);

            if (user != null) {

                user.setVoteSite(site);
            } else {

                storage.setLastVote(uniqueId, site);
            }

            this.newVotePartyAmount += 1;
            if (this.newVotePartyTask == null) {
                this.scheduleVotePartyTask();
            }
        });
    }

    private void scheduleVotePartyTask() {
        this.newVotePartyTask = this.plugin.getScheduler().runLater(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - this.newVotePartyUpdate >= 500) {

                this.handleVoteParty(this.newVotePartyAmount);
                this.newVotePartyAmount = 0;
                this.newVotePartyUpdate = 0;
                this.newVotePartyTask = null;

            } else {
                scheduleVotePartyTask();
            }
        }, 5);
    }

    private void scheduleDatabaseUpdateForPlayer(UUID uniqueId) {
        var task = this.plugin.getScheduler().runLaterAsync(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - voteCache.getLastUpdateTimestamp(uniqueId) >= 500) {
                updateDatabaseFromCacheForPlayer(uniqueId);
            } else {
                scheduleDatabaseUpdateForPlayer(uniqueId);
            }
        }, 5);
        voteCache.addTask(uniqueId, task);
    }

    private void updateDatabaseFromCacheForPlayer(UUID uniqueId) {
        var storage = this.plugin.getStorageManager().getStorage();

        UserVoteDTO voteDTO = voteCache.clearVote(uniqueId);

        if (voteDTO != null) {
            this.plugin.getScheduler().runAsync(wrappedTask -> {
                try {
                    var dto = storage.getVote(uniqueId);
                    long vote = voteDTO.vote() + dto.vote();
                    User user = this.plugin.getUser(uniqueId);
                    if (user != null) {
                        user.setVote(vote);
                    } else {
                        storage.setVote(uniqueId, vote, this.enableOfflineVoteMessage ? voteDTO.vote_offline() + dto.vote_offline() : 0);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    @Override
    public void addPlayerVote(OfflinePlayer offlinePlayer, String site) {

        if (offlinePlayer == null || offlinePlayer.getName() == null) return;

        this.onPlayerVote(offlinePlayer.getUniqueId(), site);
        var scheduler = this.plugin.getScheduler();
        scheduler.runAsync(wrappedTask -> {
            long totalVote = plugin.getStorageManager().getStorage().getVote(offlinePlayer.getUniqueId()).vote() + 1;
            var actions = this.rewardsOnVote.stream().filter(e -> totalVote >= e.min() && totalVote <= e.max()).map(VoteReward::actions).flatMap(List::stream).toList();
            scheduler.runNextTick(w2 -> actions.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", offlinePlayer.getName()))));
        });

        message(offlinePlayer.getUniqueId(), Message.COMMAND_VOTE);
    }

    @Override
    public void handleVoteParty(long amount) {

        var newAmount = this.getCurrentVotePartyAmount() + amount;

        VotePartyEvent event = new VotePartyEvent(newAmount);
        event.callEvent();

        if (event.isCancelled()) return;

        setCurrentVotePartyAmount(event.getVotePartyAmount());

        if (this.getCurrentVotePartyAmount() >= this.votePartyObjective) {

            var startEvent = new VotePartyStartEvent();
            startEvent.callEvent();

            if (event.isCancelled()) return;

            setCurrentVotePartyAmount(0);
            this.giveVotePartyRewards();
        }
    }

    private void giveVotePartyRewards() {
        this.votePartyRewards.globalCommands().forEach(command -> {
            if (command.isEmpty()) return;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });

        InventoryEngine inventoryDefault = this.plugin.getInventoryManager().getFakeInventory();
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.votePartyRewards.actions().forEach(action -> action.preExecute(player, null, inventoryDefault, new Placeholders()));

            if (player.hasPermission(this.votePartyRewards.permissions())) {
                this.votePartyRewards.permissionActions().forEach(action -> action.preExecute(player, null, inventoryDefault, new Placeholders()));
            }
        });
    }

    @Override
    public long getVotePartyObjective() {
        return this.votePartyObjective;
    }

    @Override
    public void openVoteInventory(Player player) {
        this.plugin.openInventory(player, "vote");
    }

    @Override
    public void sendVoteParty(Player player) {
        if (this.enableVotePartyOpenVoteInventory) {
            this.openVoteInventory(player);
        } else {
            message(player, Message.COMMAND_VOTEPARTY_INFORMATIONS, "%amount%", this.currentVoteAmount, "%objective%", this.votePartyObjective);
        }
    }

    @Override
    public boolean siteExist(String site) {
        return this.sites.stream().anyMatch(e -> e.name().equals(site));
    }

    @Override
    public List<VoteSiteConfiguration> getSites() {
        return this.sites;
    }

    @Override
    public long getSiteCooldown(String site) {
        return this.sites.stream().filter(voteSiteConfiguration -> voteSiteConfiguration.name().equalsIgnoreCase(site)).mapToLong(VoteSiteConfiguration::seconds).sum();
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        if (!this.enableOfflineVoteMessage) return;

        User user = this.getUser(event.getPlayer());
        if (user == null || user.getOfflineVotes() == 0) return;

        message(event.getPlayer(), Message.VOTE_OFFLINE, "%amount%", user.getOfflineVotes());
        user.resetOfflineVote();
    }

    @Override
    public String getPlaceholderCooldown() {
        return placeholderCooldown;
    }

    @Override
    public String getPlaceholderAvailable() {
        return placeholderAvailable;
    }

    @NonLoadable
    private ScheduledExecutorService resetScheduler;

    @Override
    public void onDisable() {
        if (this.resetScheduler != null && !this.resetScheduler.isShutdown()) {
            this.resetScheduler.shutdown();
        }
        super.onDisable();
    }
    
    @Override
    public void startResetTask() {

        if (!isEnable()) return;
        
        if (this.resetScheduler != null && !this.resetScheduler.isShutdown()) {
            this.resetScheduler.shutdown();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(range(this.resetConfiguration.hour(), 0, 23))
                .withMinute(range(this.resetConfiguration.minute(), 0, 59))
                .withSecond(range(this.resetConfiguration.second(), 0, 59));
        
        // Handle month day adjustment safely
        int targetDay = range(this.resetConfiguration.day(), 1, 31);
        try {
            nextRun = nextRun.withDayOfMonth(Math.min(targetDay, nextRun.toLocalDate().lengthOfMonth()));
        } catch (Exception e) {
             nextRun = nextRun.withDayOfMonth(nextRun.toLocalDate().lengthOfMonth());
        }

        if (!now.isBefore(nextRun)) {
            nextRun = nextRun.plusMonths(1);
            // Adjust day again for the next month
            try {
                nextRun = nextRun.withDayOfMonth(Math.min(targetDay, nextRun.toLocalDate().lengthOfMonth()));
            } catch (Exception e) {
                 nextRun = nextRun.withDayOfMonth(nextRun.toLocalDate().lengthOfMonth());
            }
        }
        
        long initialDelay = ChronoUnit.MILLIS.between(now, nextRun);

        this.resetScheduler = Executors.newScheduledThreadPool(1);
        // Schedule only once, then reschedule inside the task for correct monthly calculation
        this.resetScheduler.schedule(this::resetVotesAndReschedule, initialDelay, TimeUnit.MILLISECONDS);
    }
    
    private void resetVotesAndReschedule() {
        this.resetVotes();
        this.startResetTask(); // Reschedule for next month
    }

    @Override
    public void resetVotes() {
        this.plugin.getStorageManager().getStorage().resetVotes();
    }

    private int range(int value, int min, int max) {
        return value < min ? min : Math.min(value, max);
    }
}
