package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class SanctionModule extends ZModule {

    // Default messages for kick and ban
    private String kickDefaultReason;
    private String banDefaultReason;

    public SanctionModule(ZEssentialsPlugin plugin) {
        super(plugin, "sanction");
    }

    public String getKickDefaultReason() {
        return kickDefaultReason;
    }

    public String getBanDefaultReason() {
        return banDefaultReason;
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
        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        // Create and save the sanction
        Sanction sanction = Sanction.kick(uuid, getSenderUniqueId(sender), reason);
        iStorage.insertSanction(sanction, sanction::setId);

        // Kick the player with the specified reason
        server.kickPlayer(uuid, Message.MESSAGE_KICK, "%reason%", reason);

        // Broadcast a notification message to players with the kick notify permission
        server.broadcastMessage(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
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

        // Ban the player with the specified reason and duration
        server.kickPlayer(uuid, Message.MESSAGE_BAN, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));

        // Broadcast a notification message to players with the ban notify permission
        server.broadcastMessage(Permission.ESSENTIALS_BAN_NOTIFY, Message.COMMAND_BAN_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
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
            iStorage.updateMuteBan(uuid, index);

            User user = iStorage.getUser(uuid);
            if (user != null) {// If user is online, update cache
                user.setMuteSanction(sanction);
            }
        });

        // Mute the player with the specified reason and duration
        server.sendMessage(uuid, Message.MESSAGE_MUTE, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));

        // Broadcast a notification message to players with the mute notify permission
        server.broadcastMessage(Permission.ESSENTIALS_MUTE_NOTIFY, Message.COMMAND_MUTE_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTalk(AsyncChatEvent event) {

        Player player = event.getPlayer();
        IStorage iStorage = plugin.getStorageManager().getStorage();
        User user = iStorage.getUser(player.getUniqueId());
        if (user != null && user.isMute()) {
            event.setCancelled(true);
            Sanction sanction = user.getMuteSanction();
            Duration duration = sanction.getDurationRemaining();
            message(player, Message.MESSAGE_MUTE_TALK, "%reason%", sanction.getReason(), "%duration%", TimerBuilder.getStringTime(duration.toMillis()));
        }
    }
}
