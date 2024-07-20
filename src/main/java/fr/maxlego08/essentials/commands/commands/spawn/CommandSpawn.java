package fr.maxlego08.essentials.commands.commands.spawn;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SpawnModule;
import fr.maxlego08.essentials.storage.ConfigStorage;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;

public class CommandSpawn extends VCommand {

    public CommandSpawn(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SpawnModule.class);
        this.setPermission(Permission.ESSENTIALS_SPAWN);
        this.setDescription(Message.DESCRIPTION_SPAWN);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Location location = ConfigStorage.spawnLocation;
        if (location == null) {
            message(sender, Message.COMMAND_SPAWN_NOT_DEFINE);
            return CommandResultType.DEFAULT;
        }

        this.user.teleport(location, Message.TELEPORT_MESSAGE_SPAWN, Message.TELEPORT_SUCCESS_SPAWN);

        return CommandResultType.SUCCESS;
    }
}
