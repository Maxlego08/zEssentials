package fr.maxlego08.essentials.commands.commands.worldedit.options;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.worldedit.WorldeditModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandWorldEditOption extends VCommand {

    public CommandWorldEditOption(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(WorldeditModule.class);
        this.setPermission(Permission.ESSENTIALS_WORLDEDIT_OPTION);
        this.setDescription(Message.DESCRIPTION_WORLDEDIT_OPTION);
        this.addSubCommand(new CommandWorldEditOptionInventory(plugin));
        this.addSubCommand(new CommandWorldEditOptionBossBar(plugin));
        this.addSubCommand("option", "o");
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
