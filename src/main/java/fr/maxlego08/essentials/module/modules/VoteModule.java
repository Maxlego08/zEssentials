package fr.maxlego08.essentials.module.modules;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.dto.UserVoteDTO;
import fr.maxlego08.essentials.api.event.events.vote.UserVoteEvent;
import fr.maxlego08.essentials.api.event.events.vote.VotePartyEvent;
import fr.maxlego08.essentials.api.event.events.vote.VotePartyStartEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.Key;
import fr.maxlego08.essentials.api.storage.StorageKey;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.vote.VoteCache;
import fr.maxlego08.essentials.api.vote.VoteManager;
import fr.maxlego08.essentials.api.vote.VotePartyReward;
import fr.maxlego08.essentials.api.vote.VoteSiteConfiguration;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

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
    private VotePartyReward votePartyRewards;
    private List<String> rewardOnVote;
    private List<VoteSiteConfiguration> sites;

    public VoteModule(ZEssentialsPlugin plugin) {
        super(plugin, "vote");
        this.votePartyKey = new StorageKey(plugin, "vote-party-amount");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.currentVoteAmount = this.plugin.getServerStorage().getLong(this.votePartyKey);
        this.loadInventory("vote");
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
                var dto = storage.getVote(uniqueId);
                storage.setVote(uniqueId, voteDTO.vote() + dto.vote(), voteDTO.vote_offline() + dto.vote_offline());
            });
        }
    }

    @Override
    public void addPlayerVote(OfflinePlayer offlinePlayer, String site) {

        if (offlinePlayer == null || offlinePlayer.getName() == null) return;

        this.onPlayerVote(offlinePlayer.getUniqueId(), site);
        this.plugin.getScheduler().runNextTick(wrappedTask -> {
            this.rewardOnVote.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", offlinePlayer.getName())));
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
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.votePartyRewards.commands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));

            if (player.hasPermission(this.votePartyRewards.permissions())) {
                this.votePartyRewards.commandsPermission().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
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

    @EventHandler
    public void onConnect(PlayerJoinEvent event) {

        if (!this.enableOfflineVoteMessage) return;

        User user = this.getUser(event.getPlayer());
        if (user == null || user.getOfflineVotes() == 0) return;

        message(event.getPlayer(), Message.VOTE_OFFLINE, "%amount%", user.getOfflineVotes());
        user.resetOfflineVote();
    }
}
