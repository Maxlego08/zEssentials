package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.Location;

import java.util.Arrays;

public class CommandHologramYaw extends VCommandHologram {

    public CommandHologramYaw(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_YAW);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_YAW);
        this.addSubCommand("yaw");
        this.addRequireArg("yaw", (a, b) -> Arrays.asList("-180", "-135", "-90", "-45", "0", "45", "90", "135", "180"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float yaw = (float) this.argAsDouble(1);
        Location location = hologram.getLocation();
        location.setYaw(yaw);

        hologram.teleport(location);
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_YAW, "%name%", hologram.getName(), "%yaw%", yaw);
    }
}
