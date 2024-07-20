package fr.maxlego08.essentials.commands.commands.hologram.block;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.hologram.Hologram;
import fr.maxlego08.essentials.api.hologram.HologramManager;
import fr.maxlego08.essentials.api.hologram.HologramType;
import fr.maxlego08.essentials.api.hologram.configuration.BlockHologramConfiguration;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.VCommandHologram;
import org.bukkit.Material;

import java.util.Arrays;

public class CommandHologramBlock extends VCommandHologram {

    public CommandHologramBlock(EssentialsPlugin plugin) {
        super(plugin, HologramType.BLOCK);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM_ADD_LINE);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM_ADD_LINE);
        this.addSubCommand("block");
        this.addRequireArg("material", (a, b) -> Arrays.stream(Material.values()).filter(material -> material.isBlock() && !material.isLegacy()).map(Material::name).map(String::toLowerCase).toList());
    }

    @Override
    protected void perform(EssentialsPlugin plugin, Hologram hologram, HologramManager manager) {

        Material material = Material.valueOf(this.argAsString(1).toUpperCase());

        ((BlockHologramConfiguration) hologram.getConfiguration()).setMaterial(material);

        hologram.update();
        hologram.updateForAllPlayers();
        manager.saveHologram(hologram);

        message(sender, Message.HOLOGRAM_BLOCK, "%name%", hologram.getName(), "%material%", material.name());
    }
}
