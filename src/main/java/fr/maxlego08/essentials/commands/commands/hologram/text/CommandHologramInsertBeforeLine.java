package fr.maxlego08.essentials.commands.commands.hologram.text;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramLine;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

import java.util.Optional;

public class CommandHologramInsertBeforeLine extends VCommandHologram {

    public CommandHologramInsertBeforeLine(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_INSERT_BEFORE_LINE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_INSERT_BEFORE_LINE);
        this.addSubCommand("insertbefore");
        this.addRequireArgHologram("line", (sender, hologram) -> lineToList(hologram));
        this.addRequireArg("text");
        this.setExtendedArgs(true);
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        int line = this.argAsInteger(1);
        String text = this.getArgs(3);

        Optional<HologramLine> optional = hologram.getHologramLine(line);
        if (optional.isEmpty()) {
            message(sender, Message.HOLOGRAM_LINE_DOESNT_EXIST, "%name%", hologram.getName(), "%line%", line);
            return;
        }

        hologram.insertLineBefore(line, new HologramLine(line, text, false));
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_INSERT_BEFORE_LINE, "%name%", hologram.getName(), "%line%", line);
    }
}
