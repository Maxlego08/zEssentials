package fr.maxlego08.essentials.commands.commands.hologram;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramBillBoard;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramMoveHere;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramMoveTo;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramPitch;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramScale;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramTranslation;
import fr.maxlego08.essentials.commands.commands.hologram.global.CommandHologramYaw;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramAddLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramInsertAfterLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramInsertBeforeLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramRemoveLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramSetLine;
import fr.maxlego08.essentials.commands.commands.hologram.text.CommandHologramTestAlignment;
import fr.maxlego08.essentials.hologram.HologramModule;
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
        this.addSubCommand(new CommandHologramTestAlignment(plugin));
        this.addSubCommand(new CommandHologramYaw(plugin));
        this.addSubCommand(new CommandHologramPitch(plugin));
        this.addSubCommand(new CommandHologramMoveTo(plugin));
        this.addSubCommand(new CommandHologramInsertBeforeLine(plugin));
        this.addSubCommand(new CommandHologramInsertAfterLine(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
