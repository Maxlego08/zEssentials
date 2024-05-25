package fr.maxlego08.essentials.commands.commands.hologram.text;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.entity.TextDisplay;

import java.util.Arrays;

public class CommandHologramTestAlignment extends VCommandHologram {

    public CommandHologramTestAlignment(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_TEXT_ALIGNMENT);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_TEXT_ALIGNMENT);
        this.addSubCommand("textalignment");
        this.addRequireArg("text alignment", (a, b) -> Arrays.stream(TextDisplay.TextAlignment.values()).map(TextDisplay.TextAlignment::name).toList());
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        TextDisplay.TextAlignment textAlignment = TextDisplay.TextAlignment.valueOf(this.argAsString(1));
        ((TextHologramConfiguration) hologram.getConfiguration()).setTextAlignment(textAlignment);

        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_TEXT_ALIGNMENT, "%name%", hologram.getName(), "%textAlignment%", textAlignment.name());
    }
}
