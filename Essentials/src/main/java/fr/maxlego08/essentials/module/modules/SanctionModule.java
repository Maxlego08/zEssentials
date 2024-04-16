package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.server.EssentialsServer;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SanctionModule extends ZModule {

    private String kickDefaultReason;

    public SanctionModule(ZEssentialsPlugin plugin) {
        super(plugin, "sanction");
    }

    public String getKickDefaultReason() {
        return kickDefaultReason;
    }

    public void kickPlayer(CommandSender sender, Player player, String reason) {

    }

    public void kick(CommandSender sender, UUID uuid, String playerName, String reason) {
        EssentialsServer server = plugin.getEssentialsServer();
        server.kickPlayer(uuid, Message.COMMAND_KICK_RECEIVER, "%reason%", reason);
        server.broadcastMessage(Permission.ESSENTIALS_KICK_NOTIFY, Message.COMMAND_KICK_NOTIFY, "%player%", sender.getName(), "%target%", playerName, "%reason%", reason);
    }
}
