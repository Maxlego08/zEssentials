package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CommandTeleportAll extends VCommand {

    public CommandTeleportAll(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TP_ALL);
        this.setDescription(Message.DESCRIPTION_TP_ALL);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Location targetLocation = this.player.getLocation();
        if (targetLocation.getWorld() == null) return CommandResultType.SYNTAX_ERROR;

        Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(this.player))
                .forEach(p -> p.teleportAsync(targetLocation));
        message(this.sender, Message.COMMAND_TP_ALL);

        return CommandResultType.SUCCESS;
    }
}
