package fr.maxlego08.essentials.commands.commands.hologram.text;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

import java.util.Arrays;

public class CommandHologramTextShadow extends VCommandHologram {

    public CommandHologramTextShadow(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_TEXT_SHADOW);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_TEXT_SHADOW);
        this.addSubCommand("textshadow");
        this.addRequireArg("boolean", (a, b) -> Arrays.asList("true", "false"));
        this.setExtendedArgs(true);
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        boolean shadow = this.argAsBoolean(1);

        ((TextHologramConfiguration) hologram.getConfiguration()).setTextShadow(shadow);
        hologram.update();
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_SEE_THROUGH, "%name%", hologram.getName(), "%textshadow%", shadow);
    }
}
