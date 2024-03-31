package fr.maxlego08.essentials;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandManager;
import fr.maxlego08.essentials.commands.ZCommandManager;
import fr.maxlego08.essentials.commands.commands.CommandEssentials;
import fr.maxlego08.essentials.zutils.ZPlugin;

public final class ZEssentialsPlugin extends ZPlugin implements EssentialsPlugin {

    @Override
    public void onEnable() {

        this.commandManager = new ZCommandManager(this);
        this.registerCommand("zessentials", new CommandEssentials(this), "ess");

    }

    @Override
    public void onDisable() {

    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
}
