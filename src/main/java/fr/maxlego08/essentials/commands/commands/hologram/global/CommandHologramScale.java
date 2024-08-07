package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.joml.Vector3f;

import java.util.Arrays;

public class CommandHologramScale extends VCommandHologram {

    public CommandHologramScale(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_SCALE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_SCALE);
        this.addSubCommand("scale");
        this.addRequireArg("x", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
        this.addOptionalArg("y", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
        this.addOptionalArg("z", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float x = (float) this.argAsDouble(1);
        float y = (float) this.argAsDouble(2, x);
        float z = (float) this.argAsDouble(3, x);

        hologram.getConfiguration().setScale(new Vector3f(x, y, z));
        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_SCALE, "%name%", hologram.getName(), "%x%", x, "%y%", y, "%z%", z);
    }
}
