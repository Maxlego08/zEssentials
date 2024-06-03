package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.joml.Vector3f;

import java.util.Arrays;

public class CommandHologramTranslation extends VCommandHologram {

    public CommandHologramTranslation(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_TRANSLATION);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_TRANSLATION);
        this.addSubCommand("translation");
        this.addRequireArg("x", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
        this.addOptionalArg("y", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
        this.addOptionalArg("z", (a, b) -> Arrays.asList("0.5", "1", "1.5", "2"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float x = (float) this.argAsDouble(1);
        float y = (float) this.argAsDouble(2, x);
        float z = (float) this.argAsDouble(3, x);

        hologram.getConfiguration().setTranslation(new Vector3f(x, y, z));
        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_TRANSLATION, "%name%", hologram.getName(), "%x%", x, "%y%", y, "%z%", z);
    }
}
