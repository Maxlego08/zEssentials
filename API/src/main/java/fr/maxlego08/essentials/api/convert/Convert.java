package fr.maxlego08.essentials.api.convert;

import org.bukkit.command.CommandSender;

public interface Convert {

    /**
     * Converts the given sender to the object type defined in the implementing
     * class.
     *
     * @param sender The command sender to convert.
     */
    void convert(CommandSender sender);

}
