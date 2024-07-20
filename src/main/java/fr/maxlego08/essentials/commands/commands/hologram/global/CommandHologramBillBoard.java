package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.entity.Display;

import java.util.Arrays;

public class CommandHologramBillBoard extends VCommandHologram {

    public CommandHologramBillBoard(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_BILLBOARD);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_BILLBOARD);
        this.addSubCommand("billboard");
        this.addRequireArg("billdboard", (a, b) -> Arrays.stream(Display.Billboard.values()).map(Display.Billboard::name).toList());
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        Display.Billboard billboardConstraints = Display.Billboard.valueOf(this.argAsString(1));
        hologram.getConfiguration().setBillboard(billboardConstraints);

        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_BILLBOARD, "%name%", hologram.getName(), "%billboard%", billboardConstraints.name());
    }
}
