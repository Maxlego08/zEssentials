package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.hologram.Hologram;
import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface TabCompletionHologram {

	List<String> accept(CommandSender sender, Hologram hologram);
	
}