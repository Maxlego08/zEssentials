package fr.maxlego08.essentials.commands.commands.spawn;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.SpawnModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandSpawn extends VCommand {

    public CommandSpawn(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SpawnModule.class);
        this.setPermission(Permission.ESSENTIALS_SPAWN);
        this.setDescription(Message.DESCRIPTION_SPAWN);
        this.addOptionalArg("player");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0, this.player);
        if (player == null) return CommandResultType.SYNTAX_ERROR; // Only if its console

        if (this.player != null && !hasPermission(this.player, Permission.ESSENTIALS_SPAWN_OTHER)) {
            player = this.player;
        }

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return CommandResultType.SYNTAX_ERROR; // Only if its console

        Location location = ConfigStorage.spawnLocation;
        if (location == null) {
            message(sender, Message.COMMAND_SPAWN_NOT_DEFINE);
            return CommandResultType.DEFAULT;
        }

        user.teleport(location, Message.TELEPORT_MESSAGE_SPAWN, Message.TELEPORT_SUCCESS_SPAWN);
        if (this.user == null || user != this.user) {
            message(sender, Message.TELEPORT_MESSAGE_SPAWN_CONSOLE, player);
        }

        return CommandResultType.SUCCESS;
    }
}
