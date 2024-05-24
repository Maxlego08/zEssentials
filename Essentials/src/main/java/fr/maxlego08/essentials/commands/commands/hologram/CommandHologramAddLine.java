package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.hologram.ZHologramLine;

public class CommandHologramAddLine extends VCommandHologram {

    public CommandHologramAddLine(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_ADD_LINE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_ADD_LINE);
        this.addSubCommand("addline");
        this.addRequireArg("text");
        this.setExtendedArgs(true);
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        String text = this.getArgs(2);
        int nextIndex = hologram.getNextIndex();
        hologram.addLine(new ZHologramLine(nextIndex, text));
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_ADD_LINE, "%name%", hologram.getName());
    }
}
