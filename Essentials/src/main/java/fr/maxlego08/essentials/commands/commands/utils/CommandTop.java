package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.Location;

import java.util.Optional;

public class CommandTop extends VCommand {
    public CommandTop(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_TOP);
        this.setDescription(Message.DESCRIPTION_NIGHT);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Optional<Location> optional = this.topLocation(this.player.getLocation(), 0, this.player.getWorld().getMaxHeight());
        if (optional.isPresent()) {

            Location location = optional.get();
            location.setPitch(this.player.getLocation().getPitch());
            location.setYaw(this.player.getLocation().getYaw());
            location.add(0.5, 0, 0.5);
            this.player.teleport(location);
            message(this.sender, Message.COMMAND_TOP);
        } else {

            message(this.sender, Message.COMMAND_TOP_ERROR);
        }

        return CommandResultType.SUCCESS;
    }
}
