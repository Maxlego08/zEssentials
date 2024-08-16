package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.commands.commands.worldedit.options.CommandWorldEditOption;
import fr.maxlego08.essentials.commands.commands.worldedit.options.CommandWorldEditOptionInventory;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEdit extends VCommand {

    public CommandWorldEdit(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_USE);
        this.addSubCommand(new CommandWorldEditConfirm(plugin));
        this.addSubCommand(new CommandWorldEditCut(plugin));
        this.addSubCommand(new CommandWorldEditFill(plugin));
        this.addSubCommand(new CommandWorldEditGive(plugin));
        this.addSubCommand(new CommandWorldEditSet(plugin));
        this.addSubCommand(new CommandWorldEditStop(plugin));
        this.addSubCommand(new CommandWorldEditWalls(plugin));
        this.addSubCommand(new CommandWorldEditSphere(plugin));
        this.addSubCommand(new CommandWorldEditPos1(plugin));
        this.addSubCommand(new CommandWorldEditPos2(plugin));
        this.addSubCommand(new CommandWorldEditCyl(plugin));
        this.addSubCommand(new CommandWorldEditOption(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
