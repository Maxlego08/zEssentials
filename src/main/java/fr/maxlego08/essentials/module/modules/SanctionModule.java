package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.cache.ExpiringCache;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.UserDTO;
import fr.maxlego08.essentials.api.event.events.user.UserJoinEvent;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.sanction.SanctionType;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.user.UserRecord;
import fr.maxlego08.essentials.listener.paper.ChatListener;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.user.ZUser;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SanctionModule extends ZModule {

    private final ExpiringCache<UUID, User> expiringCache = new ExpiringCache<>(1000 * 60 * 60); // 1 hour cache
    // Default messages for kick and ban
    private final String kickDefaultReason = "";
    private final String banDefaultReason = "";
    private final String muteDefaultReason = "";
    private final String unmuteDefaultReason = "";
    private final String unbanDefaultReason = "";
    private final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private final Material kickMaterial = Material.BOOK;
    private final Material banMaterial = Material.BOOK;
    private final Material muteMaterial = Material.BOOK;
    private final Material unbanMaterial = Material.BOOK;
    private final Material unmuteMaterial = Material.BOOK;
    private final Material warnMaterial = Material.BOOK;
    private final Material freezeMaterial = Material.BOOK;
    private final Material currentMuteMaterial = Material.BOOKSHELF;
    private final Material currentBanMaterial = Material.BOOKSHELF;
    private final List<String> protections = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;


    public SanctionModule(ZEssentialsPlugin plugin) {
        super(plugin, "sanction");
        Bukkit.getPluginManager().registerEvents(isPaperVersion() ? new ChatListener(plugin) : new fr.maxlego08.essentials.listener.spigot.ChatListener(plugin), plugin);
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("sanction");
        this.loadInventory("sanction_history");
        this.loadInventory("sanctions");
        this.simpleDateFormat = new SimpleDateFormat(this.dateFormat);
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getKickDefaultReason() {
        return kickDefaultReason;
    }

    public String getBanDefaultReason() {
        return banDefaultReason;
    }

    public String getMuteDefaultReason() {
        return muteDefaultReason;
    }

    public String getUnbanDefaultReason() {
        return unbanDefaultReason;
    }

    public String getUnmuteDefaultReason() {
        return unmuteDefaultReason;
    }

    public Material getSanctionMaterial(SanctionType sanctionType, boolean isActive) {
        return switch (sanctionType) {
            case KICK -> kickMaterial;
            case MUTE -> isActive ? currentMuteMaterial : muteMaterial;
            case BAN -> isActive ? currentBanMaterial : banMaterial;
            case UNBAN -> unbanMaterial;
            case UNMUTE -> unmuteMaterial;
            case WARN -> warnMaterial;
            case FREEZE -> freezeMaterial;
        };
    }


    // Get the UUID of the sender (player or console)
    private UUID getSenderUniqueId(CommandSender sender) {
        return sender instanceof Player player ? player.getUniqueId() : this.plugin.getConsoleUniqueId();
    }

    /**
     * Kick a player with a specified reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to kick.
     * @param playerName The name of the player to kick.
     * @param reason     The reason for the kick.
     */
    public void kick(CommandSender sender, UUID uuid, String playerName, String reason) {

        if (isProtected(playerName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Create and save the sanction
        Sanction sanction = Sanction.kick(uuid, getSenderUniqueId(sender), reason);
        iStorage.insertSanction(sanction, sanction::setId);
        this.expiringCache.clear(uuid);

        // Kick the player with the specified reason
        server.kickPlayer(uuid, Message.MESSAGE_KICK, "%reason%", reason);

        // Broadcast a notification message to players with the kick notify permission
        server.broadcastMessage(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason, "%sender%", getSanctionBy(sender), "%created_at%", this.simpleDateFormat.format(new Date()));
    }

    /**
     * Ban a player for a specified duration with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to ban.
     * @param playerName The name of the player to ban.
     * @param duration   The duration of the ban.
     * @param reason     The reason for the ban.
     */
    public void ban(CommandSender sender, UUID uuid, String playerName, Duration duration, String reason) {

        if (isProtected(playerName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Check if the ban duration is valid
        if (duration.isZero()) {
            message(sender, Message.COMMAND_BAN_ERROR_DURATION);
            return;
        }

        // Calculate the ban finish date
        Date finishAt = new Date(System.currentTimeMillis() + duration.toMillis());

        // Create and save the sanction
        Sanction sanction = Sanction.ban(uuid, getSenderUniqueId(sender), reason, duration, finishAt);
        iStorage.insertSanction(sanction, index -> {
            sanction.setId(index);
            iStorage.updateUserBan(uuid, index);
        });
        this.expiringCache.clear(uuid);

        String durationString = TimerBuilder.getStringTime(duration.toMillis());
        // Ban the player with the specified reason and duration
        server.kickPlayer(uuid, Message.MESSAGE_BAN, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));

        // Broadcast a notification message to players with the ban notify permission
        server.broadcastMessage(Permission.ESSENTIALS_BAN_NOTIFY, Message.COMMAND_BAN_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason, "%duration%", durationString, "%sender%", getSanctionBy(sender), "%created_at%", this.simpleDateFormat.format(new Date()), "%expired_at%", this.simpleDateFormat.format(sanction.getExpiredAt()));
    }

    /**
     * Mute a player for a specified duration with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to mute.
     * @param playerName The name of the player to mute.
     * @param duration   The duration of the mute.
     * @param reason     The reason for the mute.
     */
    public void mute(CommandSender sender, UUID uuid, String playerName, Duration duration, String reason) {

        if (isProtected(playerName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Check if the mute duration is valid
        if (duration.isZero()) {
            message(sender, Message.COMMAND_MUTE_ERROR_DURATION);
            return;
        }

        // Calculate the mute finish date
        Date finishAt = new Date(System.currentTimeMillis() + duration.toMillis());

        // Create and save the sanction
        Sanction sanction = Sanction.mute(uuid, getSenderUniqueId(sender), reason, duration, finishAt);
        iStorage.insertSanction(sanction, index -> {
            sanction.setId(index);
            iStorage.updateUserMute(uuid, index);

            User user = iStorage.getUser(uuid);
            if (user != null) {// If user is online, update cache
                user.setMuteSanction(sanction);
            }
        });
        this.expiringCache.clear(uuid);

        // Mute the player with the specified reason and duration
        server.sendMessage(uuid, Message.MESSAGE_MUTE, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));

        // Broadcast a notification message to players with the mute notify permission
        server.broadcastMessage(Permission.ESSENTIALS_MUTE_NOTIFY, Message.COMMAND_MUTE_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()), "%sender%", getSanctionBy(sender), "%created_at%", this.simpleDateFormat.format(new Date()), "%expired_at%", this.simpleDateFormat.format(sanction.getExpiredAt()));
    }

    /**
     * UnMute a player with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to unmute.
     * @param playerName The name of the player to unmute.
     * @param reason     The reason for to unmute.
     */
    public void unmute(CommandSender sender, UUID uuid, String playerName, String reason) {

        if (isProtected(playerName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        IStorage iStorage = plugin.getStorageManager().getStorage();

        User user = iStorage.getUser(uuid);
        if (user == null) {
            // Check is user is mute
            this.plugin.getScheduler().runAsync(wrappedTask -> {
                if (!iStorage.isMute(uuid)) {
                    message(sender, Message.COMMAND_UN_MUTE_ERROR, "%player%", playerName);
                    return;
                }

                processUnmute(sender, uuid, playerName, reason);
            });
        } else {
            // Check is user is mute
            if (!user.isMute()) {
                message(sender, Message.COMMAND_UN_MUTE_ERROR, "%player%", playerName);
                return;
            }

            processUnmute(sender, uuid, playerName, reason);
        }
    }

    private void processUnmute(CommandSender sender, UUID uuid, String playerName, String reason) {

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Create and save the sanction
        Sanction sanction = Sanction.unmute(uuid, getSenderUniqueId(sender), reason);
        iStorage.insertSanction(sanction, index -> {
            sanction.setId(index);
            iStorage.updateUserMute(uuid, null);

            User user = iStorage.getUser(uuid);
            if (user != null) { // If user is online, update cache
                user.setMuteSanction(null);
            }
        });
        this.expiringCache.clear(uuid);

        // Mute the player with the specified reason and duration
        server.sendMessage(uuid, Message.MESSAGE_UNMUTE, "%reason%", reason);

        // Broadcast a notification message to players with the mute notify permission
        server.broadcastMessage(Permission.ESSENTIALS_UNMUTE_NOTIFY, Message.COMMAND_UNMUTE_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason, "%sender%", getSanctionBy(sender), "%created_at%", this.simpleDateFormat.format(new Date()), "%duration%", "0");
    }

    /**
     * UnBan a player with a reason.
     *
     * @param sender     The command sender.
     * @param uuid       The UUID of the player to unban.
     * @param playerName The name of the player to unban.
     * @param reason     The reason for the unban.
     */
    public void unban(CommandSender sender, UUID uuid, String playerName, String reason) {

        if (isProtected(playerName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();
        if (!iStorage.isBan(uuid)) {
            message(sender, Message.COMMAND_UN_BAN_ERROR, "%player%", playerName);
            return;
        }

        // Create and save the sanction
        Sanction sanction = Sanction.unban(uuid, getSenderUniqueId(sender), reason);
        iStorage.insertSanction(sanction, index -> {
            sanction.setId(index);
            iStorage.updateUserBan(uuid, null);
        });
        this.expiringCache.clear(uuid);

        // Broadcast a notification message to players with the mute notify permission
        server.broadcastMessage(Permission.ESSENTIALS_UNBAN_NOTIFY, Message.COMMAND_UNBAN_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason, "%sender%", getSanctionBy(sender), "%created_at%", this.simpleDateFormat.format(new Date()), "%duration%", "0");
    }

    public void openSanction(User user, UUID uuid, String userName) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        this.plugin.getScheduler().runAsync(wrappedTask -> {

            user.setTargetUser(expiringCache.get(uuid, () -> {
                User fakeUser = ZUser.fakeUser(this.plugin, uuid, userName);
                Sanction muteSanction = iStorage.getMute(uuid);
                fakeUser.setFakeOption(Option.BAN, iStorage.isBan(uuid));
                fakeUser.setFakeOption(Option.MUTE, muteSanction != null && muteSanction.isActive());
                fakeUser.setMuteSanction(muteSanction);
                fakeUser.setBanSanction(iStorage.getBan(uuid));
                fakeUser.setFakeSanctions(iStorage.getSanctions(uuid));
                return fakeUser;
            }));

            this.plugin.openInventory(user.getPlayer(), "sanction");
        });
    }

    public String getSanctionBy(UUID senderUniqueId) {
        return senderUniqueId.equals(this.plugin.getConsoleUniqueId()) ? Message.CONSOLE.getMessageAsString() : Bukkit.getOfflinePlayer(senderUniqueId).getName();
    }

    private String getSanctionBy(CommandSender sender) {
        return sender instanceof Player player ? player.getName() : Message.CONSOLE.getMessageAsString();
    }

    private boolean isProtected(String username) {
        return this.protections.stream().anyMatch(name -> name.equalsIgnoreCase(username));
    }

    public void seen(CommandSender sender, UUID uuid) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        UserRecord record = iStorage.fetchUserRecord(uuid);
        UserDTO user = record.userDTO();

        boolean isOnline = Bukkit.getPlayer(user.unique_id()) != null;
        if (isOnline) sendOnline(sender, record);
        else sendOffline(sender, record);

        message(sender, Message.COMMAND_SEEN_UUID, "%uuid%", uuid.toString());
        message(sender, Message.COMMAND_SEEN_IP, "%ips%", record.playTimeDTOS().stream().map(timeDTO -> getMessage(Message.COMMAND_SEEN_ADDRESS, "%ip%", timeDTO.address())).distinct().collect(Collectors.joining(",")));
        Location location = stringAsLocation(user.last_location());
        message(sender, Message.COMMAND_SEEN_LAST_LOCATION, "%x%", location.getBlockX(), "%z%", location.getBlockZ(), "%y%", location.getBlockY(), "%world%", location.getWorld().getName());
        message(sender, Message.COMMAND_SEEN_FIRST_JOIN, "%created_at%", this.simpleDateFormat.format(user.created_at()));
    }

    private void sendOnline(CommandSender sender, UserRecord record) {
        User user = this.plugin.getUser(record.userDTO().unique_id());
        message(sender, Message.COMMAND_SEEN_ONLINE, "%player%", record.userDTO().name(), "%date%", TimerBuilder.getStringTime(System.currentTimeMillis() - user.getCurrentSessionPlayTime()));
        message(sender, Message.COMMAND_SEEN_PLAYTIME, "%playtime%", TimerBuilder.getStringTime(user.getPlayTime() * 1000));
    }

    private void sendOffline(CommandSender sender, UserRecord record) {
        message(sender, Message.COMMAND_SEEN_OFFLINE, "%player%", record.userDTO().name(), "%date%", this.simpleDateFormat.format(record.userDTO().updated_at()));
        message(sender, Message.COMMAND_SEEN_PLAYTIME, "%playtime%", TimerBuilder.getStringTime(record.userDTO().play_time() * 1000));
    }

    public void seen(CommandSender sender, String ip) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        List<UserDTO> userDTOS = iStorage.getUsers(ip);

        if (userDTOS.isEmpty()) {
            message(sender, Message.COMMAND_SEEN_IP_EMPTY, "%ip%", ip);
            return;
        }

        message(sender, Message.COMMAND_SEEN_IP_LINE, "%ip%", ip, "%players%", userDTOS.stream().map(user -> getMessage(Message.COMMAND_SEEN_IP_INFO, "%name%", user.name())).collect(Collectors.joining(",")));
    }

    public void freeze(CommandSender sender, UUID uuid, String userName) {

        if (isProtected(userName)) {
            message(sender, Message.COMMAND_SANCTION_ERROR);
            return;
        }

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(uuid);
        if (user == null) {
            message(sender, Message.PLAYER_NOT_FOUND, "%player%", userName);
            return;
        }

        user.setFrozen(!user.isFrozen());

        Sanction sanction = Sanction.freeze(uuid, getSenderUniqueId(sender));
        iStorage.insertSanction(sanction, index -> {
            sanction.setId(index);
            iStorage.updateUserFrozen(uuid, user.isFrozen());
        });
        if (user.isFrozen()) {
            message(sender, Message.COMMAND_FREEZE_SUCCESS, "%player%", userName);
            this.plugin.getEssentialsServer().sendMessage(uuid, Message.MESSAGE_FREEZE);

            user.getPlayer().setAllowFlight(true);
            user.getPlayer().setFlying(true);
            user.getPlayer().setFlySpeed(0f);
            this.plugin.getScheduler().teleportAsync(user.getPlayer(), user.getPlayer().getLocation().add(0, 0.1, 0));
        } else {
            user.getPlayer().setAllowFlight(false);
            user.getPlayer().setFlying(false);
            message(sender, Message.COMMAND_UN_FREEZE_SUCCESS, "%player%", userName);
            this.plugin.getEssentialsServer().sendMessage(uuid, Message.MESSAGE_UN_FREEZE);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(event.getPlayer().getUniqueId());
        if (user.isFrozen()) {
            user.getPlayer().setAllowFlight(true);
            user.getPlayer().setFlying(true);
            user.getPlayer().setFlySpeed(0f);
            this.plugin.getScheduler().teleportAsync(user.getPlayer(), user.getPlayer().getLocation().add(0, 0.1, 0));
            this.plugin.getEssentialsServer().sendMessage(user.getUniqueId(), Message.MESSAGE_FREEZE);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        User user = this.plugin.getUser(event.getPlayer().getUniqueId());
        if (user.isFrozen()) {
            event.setCancelled(true);
            this.plugin.getEssentialsServer().sendMessage(user.getUniqueId(), Message.MESSAGE_FREEZE);
        }
    }
}
