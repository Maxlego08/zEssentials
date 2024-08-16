package fr.maxlego08.essentials.commands.commands.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditPos1 extends VCommand {

    public CommandWorldEditPos1(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_POS1);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_POS1);
        this.addSubCommand("pos1");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getWorldeditManager().setPos1(this.player, this.player.getLocation());

        return CommandResultType.SUCCESS;
    }
}
