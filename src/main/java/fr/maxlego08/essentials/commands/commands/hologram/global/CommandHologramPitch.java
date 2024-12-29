package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.SafeLocation;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.Location;

import java.util.Arrays;

public class CommandHologramPitch extends VCommandHologram {

    public CommandHologramPitch(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_YAW);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_YAW);
        this.addSubCommand("pitch");
        this.addRequireArg("pitch", (a, b) -> Arrays.asList("-180", "-135", "-90", "-45", "0", "45", "90", "135", "180"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float pitch = (float) this.argAsDouble(1);
        SafeLocation location = hologram.getLocation();
        location.setPitch(pitch);

        hologram.teleport(location.getLocation());
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_PITCH, "%name%", hologram.getName(), "%pitch%", pitch);
    }
}
