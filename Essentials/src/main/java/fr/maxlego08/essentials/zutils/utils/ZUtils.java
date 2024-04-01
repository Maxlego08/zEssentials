package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.commands.Permission;
import org.bukkit.command.CommandSender;

public abstract class ZUtils extends MessageUtils {

    protected boolean hasPermission(CommandSender sender, Permission permission) {
        return sender.hasPermission(permission.asPermission());
    }

    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
