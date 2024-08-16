package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditCut extends VCommand {

    public CommandWorldEditCut(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_CUT);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_CUT);
        this.addSubCommand("cut");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getWorldeditManager().cutBlocks(this.user);

        return CommandResultType.SUCCESS;
    }
}
