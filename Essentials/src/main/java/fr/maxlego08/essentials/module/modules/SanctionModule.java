package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.sanction.Sanction;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

public class SanctionModule extends ZModule {

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

    private UUID getSenderUniqueId(CommandSender sender) {
        return sender instanceof Player player ? player.getUniqueId() : this.plugin.getConsoleUniqueId();
    }

    public void kick(CommandSender sender, UUID uuid, String playerName, String reason) {
        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        Sanction sanction = Sanction.kick(uuid, getSenderUniqueId(sender), reason);
        iStorage.insertSanction(sanction);

        server.kickPlayer(uuid, Message.MESSAGE_KICK, "%reason%", reason);
        server.broadcastMessage(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
    }

    public void ban(CommandSender sender, UUID uuid, String playerName, Duration duration, String reason) {

        EssentialsServer server = plugin.getEssentialsServer();
        IStorage iStorage = plugin.getStorageManager().getStorage();

        if (duration.isZero()) {
            message(sender, Message.COMMAND_BAN_ERROR_DURATION);
            return;
        }

        Date finishAt = new Date(System.currentTimeMillis() + duration.toMillis());
        Sanction sanction = Sanction.ban(uuid, getSenderUniqueId(sender), reason, duration, finishAt);
        iStorage.insertSanction(sanction);

        server.kickPlayer(uuid, Message.MESSAGE_BAN, "%reason%", reason, "%duration%", TimerBuilder.getStringTime(duration.toMillis()));
        server.broadcastMessage(Permission.ESSENTIALS_BAN_NOTIFY, Message.COMMAND_BAN_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
    }
}
