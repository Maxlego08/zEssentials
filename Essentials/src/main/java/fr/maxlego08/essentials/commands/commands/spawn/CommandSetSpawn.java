package fr.maxlego08.essentials.commands.commands.spawn;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.SpawnModule;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CommandSetSpawn extends VCommand {

    public CommandSetSpawn(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(SpawnModule.class);
        this.setPermission(Permission.ESSENTIALS_SET_SPAWN);
        this.setDescription(Message.DESCRIPTION_SET_SPAWN);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Location location = this.player.getLocation();
        plugin.getServerStorage().setSpawnLocation(location);
        plugin.getStorageManager().getStorage().upsertStorage("spawn_location", locationAsString(location));
        message(sender, Message.COMMAND_SET_SPAWN);

        return CommandResultType.SUCCESS;
    }
}
