package fr.maxlego08.essentials.commands.commands.hologram.text;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.TextHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class CommandHologramBackground extends VCommandHologram {

    public CommandHologramBackground(EssentialsPlugin plugin) {
        super(plugin, HologramType.TEXT);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_TEXT_BACKGROUND);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_TEXT_BACKGROUND);
        this.addSubCommand("background");
        this.addRequireArg("background", (a, b) -> {
            List<String> colors = new ArrayList<>(NamedTextColor.NAMES.keys());
            colors.add("reset");
            colors.add("default");
            colors.add("transparent");
            return colors;
        });
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        String color = this.argAsString(1);
        TextColor background;

        if ("reset".equals(color) || "default".equals(color)) background = null;
        else {
            if ("transparent".equals(color)) background = Hologram.TRANSPARENT;
            else if (color.startsWith("#")) background = TextColor.fromHexString(color);
            else background = NamedTextColor.NAMES.value(color.replace(' ', '_'));

            if (background == null) {
                message(sender, Message.HOLOGRAM_TEXT_BACKGROUND_ERROR);
                return;
            }
        }

        ((TextHologramConfiguration) hologram.getConfiguration()).setBackground(background);

        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_TEXT_BACKGROUND, "%name%", hologram.getName(), "%color%", color);
    }
}
