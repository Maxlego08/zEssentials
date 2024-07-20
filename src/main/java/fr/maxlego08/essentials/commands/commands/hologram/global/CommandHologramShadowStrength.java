package fr.maxlego08.essentials.commands.commands.hologram.global;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;

import java.util.Arrays;

public class CommandHologramShadowStrength extends VCommandHologram {

    public CommandHologramShadowStrength(EssentialsPlugin plugin) {
        super(plugin, null);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_SHADOW_STRENGTH);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_SHADOW_STRENGTH);
        this.addSubCommand("shadowstrength");
        this.addRequireArg("strength", (a, b) -> Arrays.asList("0", "1"));
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        float shadow = (float) this.argAsDouble(1);

        hologram.getConfiguration().setShadowStrength(shadow);
        hologram.update();
        hologram.updateForAllPlayers();

        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_SHADOW_STRENGTH, "%name%", hologram.getName(), "%shadow%", shadow);
    }
}
