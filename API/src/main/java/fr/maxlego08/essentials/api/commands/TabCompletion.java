package fr.maxlego08.essentials.api.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface TabCompletion {

	/**
	 * Provides a list of tab completions based on the sender and command arguments.
	 *
	 * @param sender The command sender requesting tab completions.
	 * @param args   The command arguments provided by the sender.
	 * @return A list of possible tab completions.
	 */
	List<String> accept(CommandSender sender, String[] args);
	
}