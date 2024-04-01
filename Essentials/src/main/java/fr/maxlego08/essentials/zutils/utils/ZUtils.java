package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public abstract class ZUtils extends MessageUtils {

    protected boolean hasPermission(CommandSender sender, Permission permission) {
        return sender.hasPermission(permission.asPermission());
    }

    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    protected Optional<Location> topLocation(Location location, int step, int y) {

        if (step > location.getWorld().getMaxHeight()) {
            return Optional.empty();
        }

        location.setY(y);
        if (!location.getBlock().getType().isSolid() && !location.getBlock().getRelative(BlockFace.UP).getType().isSolid() && location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
            return Optional.of(location);
        }
        return this.topLocation(location.getBlock().getRelative(BlockFace.UP).getLocation(), step + 1, y - 1);

    }

}
