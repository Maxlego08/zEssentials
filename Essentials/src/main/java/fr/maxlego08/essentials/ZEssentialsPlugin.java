package fr.maxlego08.essentials;

import fr.maxlego08.essentials.api.ConfigurationFile;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.commands.CommandLoader;
import fr.maxlego08.essentials.commands.ZCommandManager;
import fr.maxlego08.essentials.commands.commands.essentials.CommandEssentials;
import fr.maxlego08.essentials.messages.MessageLoader;
import fr.maxlego08.essentials.zutils.ZPlugin;

import java.util.List;

public final class ZEssentialsPlugin extends ZPlugin implements EssentialsPlugin {

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        // Configurations files
        this.registerConfiguration(new MessageLoader(this));

        // Load configuration files
        this.configurationFiles.forEach(ConfigurationFile::load);

        // Commands
        this.commandManager = new ZCommandManager(this);
        this.registerCommand("zessentials", new CommandEssentials(this), "ess");

        CommandLoader commandLoader = new CommandLoader(this);
        commandLoader.loadCommands(this.commandManager);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public List<ConfigurationFile> getConfigurationFiles() {
        return this.configurationFiles;
    }
}
