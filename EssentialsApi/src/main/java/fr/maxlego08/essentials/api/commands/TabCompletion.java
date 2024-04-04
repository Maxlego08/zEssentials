package fr.maxlego08.essentials.api.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

@FunctionalInterface
public interface TabCompletion {

	List<String> accept(CommandSender sender, String[] args);
	
}