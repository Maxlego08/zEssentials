package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.module.modules.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditCancel extends VCommand {

    public CommandWorldEditCancel(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_CANCEL);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_CANCEL);
        this.addSubCommand("cancel");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getWorldeditManager().cancelSelection(this.user);

        return CommandResultType.SUCCESS;
    }
}
