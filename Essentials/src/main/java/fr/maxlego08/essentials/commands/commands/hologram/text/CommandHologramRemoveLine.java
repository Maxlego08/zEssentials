package fr.maxlego08.essentials.commands.commands.hologram.text;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

public class CommandHologramRemoveLine extends VCommandHologram {

    public CommandHologramRemoveLine(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_REMOVE_LINE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_REMOVE_LINE);
        this.addSubCommand("removeline");
        this.addRequireArgHologram("line", (sender, hologram) -> lineToList(hologram));
        this.setExtendedArgs(true);
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        int line = this.argAsInteger(1);

        if (hologram.getHologramLine(line).isEmpty()) {
            message(sender, Message.HOLOGRAM_LINE_DOESNT_EXIST, "%name%", hologram.getName(), "%line%", line);
            return;
        }

        hologram.removeLine(line);
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_REMOVE_LINE, "%name%", hologram.getName(), "%line%", line);
    }
}
