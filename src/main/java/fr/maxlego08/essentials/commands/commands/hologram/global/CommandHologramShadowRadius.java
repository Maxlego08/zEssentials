package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

import java.util.Arrays;

public class CommandHologramShadowRadius extends VCommandHologram {

    public CommandHologramShadowRadius(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_SHADOW_RADIUS);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_SHADOW_RADIUS);
        this.addSubCommand("shadowradius");
        this.addRequireArg("radius", (a, b) -> Arrays.asList("0", "1"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float shadow = (float) this.argAsDouble(1);

        hologram.getConfiguration().setShadowRadius(shadow);
        hologram.update();
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_SHADOW_RADIUS, "%name%", hologram.getName(), "%shadow%", shadow);
    }
}
