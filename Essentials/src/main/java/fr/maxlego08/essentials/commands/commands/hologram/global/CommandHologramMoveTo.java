package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHologramMoveTo extends VCommandHologram {

    public CommandHologramMoveTo(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_MOVE_TO);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_MOVE_TO);
        this.addSubCommand("moveto");
        this.addRequireArg("x", (sender, args) -> sender instanceof Player player ? List.of(String.valueOf(player.getLocation().getBlockX())) : new ArrayList<>());
        this.addRequireArg("y", (sender, args) -> sender instanceof Player player ? List.of(String.valueOf(player.getLocation().getBlockY())) : new ArrayList<>());
        this.addRequireArg("z", (sender, args) -> sender instanceof Player player ? List.of(String.valueOf(player.getLocation().getBlockZ())) : new ArrayList<>());
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        double x = this.argAsDouble(1);
        double y = this.argAsDouble(2);
        double z = this.argAsDouble(3);

        Location location = hologram.getLocation();
        location.set(x, y, z);

        hologram.teleport(location);

        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_MOVE_TO, "%name%", hologram.getName(), "%x%", x, "%y%", y, "%z%", z);
    }
}
