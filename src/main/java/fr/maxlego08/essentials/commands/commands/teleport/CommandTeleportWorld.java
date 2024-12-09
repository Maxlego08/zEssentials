package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;

public class CommandTeleportWorld extends VCommand {

    public CommandTeleportWorld(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TP_WORLD);
        this.setDescription(Message.DESCRIPTION_TP_WORLD);
        this.addRequireArg("world", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).toList());
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        World world = this.argAsWorld(0);
        Player player = this.argAsPlayer(1, this.player);

        if (player == null || world == null) return CommandResultType.SYNTAX_ERROR;

        player.teleportAsync(world.getSpawnLocation());

        if (player == this.player) {

            message(sender, Message.COMMAND_WORLD_TELEPORT_SELF, "%world%", world.getName());
        } else {

            if (!hasPermission(sender, Permission.ESSENTIALS_TP_WORLD_OTHER)) {
                return CommandResultType.NO_PERMISSION;
            }

            message(player, Message.COMMAND_WORLD_TELEPORT_SELF, "%world%", world.getName());
            message(sender, Message.COMMAND_WORLD_TELEPORT_OTHER, "%world%", world.getName(), player);
        }

        return CommandResultType.SUCCESS;
    }
}
