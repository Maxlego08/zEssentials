package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

import java.util.Arrays;

public class CommandHologramViewDistance extends VCommandHologram {

    public CommandHologramViewDistance(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_SHADOW_STRENGTH);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_SHADOW_STRENGTH);
        this.addSubCommand("viewdistance");
        this.addRequireArg("distance", (a, b) -> Arrays.asList("10", "20", "30", "40", "50", "60"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        int distance = this.argAsInteger(1);

        hologram.getConfiguration().setVisibilityDistance(distance);
        hologram.update();
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_VIEW_DISTANCE, "%name%", hologram.getName(), "%distance%", distance);
    }
}
