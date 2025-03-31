package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.block.CommandHologramBlock;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramBillBoard;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramMoveHere;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramMoveTo;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramPitch;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramScale;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramShadowRadius;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramShadowStrength;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramTeleport;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramTranslation;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramViewDistance;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramYaw;
import fr.maxlego08.essentials.commands.commands.hologram.item.CommandHologramItem;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramAddLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramBackground;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramInsertAfterLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramInsertBeforeLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramRemoveLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramSetLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramTextAlignment;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramTextSeeThrough;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramTextShadow;
import fr.maxlego08.essentials.module.modules.hologram.HologramModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandHologram extends VCommand {

    public CommandHologram(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(HologramModule.class);
        this.setPermission(Permission.ESSENTIALS_HOLOGRAM);
        this.setDescription(Message.DESCRIPTION_HOLOGRAM);

        this.addSubCommand(new CommandHologramCreate(plugin));
        this.addSubCommand(new CommandHologramDelete(plugin));
        this.addSubCommand(new CommandHologramAddLine(plugin));
        this.addSubCommand(new CommandHologramSetLine(plugin));
        this.addSubCommand(new CommandHologramRemoveLine(plugin));
        this.addSubCommand(new CommandHologramScale(plugin));
        this.addSubCommand(new CommandHologramTranslation(plugin));
        this.addSubCommand(new CommandHologramMoveHere(plugin));
        this.addSubCommand(new CommandHologramBillBoard(plugin));
        this.addSubCommand(new CommandHologramTextAlignment(plugin));
        this.addSubCommand(new CommandHologramYaw(plugin));
        this.addSubCommand(new CommandHologramPitch(plugin));
        this.addSubCommand(new CommandHologramMoveTo(plugin));
        this.addSubCommand(new CommandHologramInsertBeforeLine(plugin));
        this.addSubCommand(new CommandHologramInsertAfterLine(plugin));
        this.addSubCommand(new CommandHologramBackground(plugin));
        this.addSubCommand(new CommandHologramList(plugin));
        this.addSubCommand(new CommandHologramTeleport(plugin));
        this.addSubCommand(new CommandHologramTextSeeThrough(plugin));
        this.addSubCommand(new CommandHologramTextShadow(plugin));
        this.addSubCommand(new CommandHologramShadowStrength(plugin));
        this.addSubCommand(new CommandHologramShadowRadius(plugin));
        this.addSubCommand(new CommandHologramViewDistance(plugin));
        this.addSubCommand(new CommandHologramItem(plugin));
        this.addSubCommand(new CommandHologramBlock(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
