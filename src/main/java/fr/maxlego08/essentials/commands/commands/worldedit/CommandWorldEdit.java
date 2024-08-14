package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
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
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
