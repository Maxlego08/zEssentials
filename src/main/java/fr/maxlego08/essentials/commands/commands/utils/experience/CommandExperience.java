package fr.maxlego08.essentials.commands.commands.utils.experience;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandExperience extends VCommand {

    public CommandExperience(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_EXPERIENCE);
        this.setDescription(Message.DESCRIPTION_EXPERIENCE);
        this.addSubCommand(new CommandGrantExperience(plugin));
        this.addSubCommand(new CommandSetExperience(plugin));
        this.addSubCommand(new CommandQueryExperience(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
