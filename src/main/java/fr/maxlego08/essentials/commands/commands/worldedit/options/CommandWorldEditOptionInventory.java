package fr.maxlego08.essentials.commands.commands.worldedit.options;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditOptionInventory extends VCommand {

    public CommandWorldEditOptionInventory(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_OPTION_INVENTORY);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_OPTION_INVENTORY);
        this.addSubCommand("inventory", "inv");
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        plugin.getWorldeditManager().toggleOptionInventory(this.user);

        return CommandResultType.SUCCESS;
    }

}
