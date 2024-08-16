package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditStop extends VCommand {

    public CommandWorldEditStop(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_STOP);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_STOP);
        this.addSubCommand("stop", "cancel");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getWorldeditManager().stopEdition(this.user);

        return CommandResultType.SUCCESS;
    }
}
