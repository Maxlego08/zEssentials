package fr.maxlego08.essentials.zutils;

import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class ZPlugin extends JavaPlugin {

    protected CommandManager commandManager;

    protected void registerCommand(String command, VCommand vCommand, String... aliases) {
        this.commandManager.registerCommand(this, command, vCommand, Arrays.asList(aliases));
    }


}
