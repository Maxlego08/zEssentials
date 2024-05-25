package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.Location;

public class CommandHologramTeleport extends VCommandHologram {

    public CommandHologramTeleport(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_TELEPORT);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_TELEPORT);
        this.addSubCommand("teleport", "tp");
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {
        Location location = hologram.getLocation();
        player.teleportAsync(location);
    }
}
