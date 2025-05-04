package fr.maxlego08.essentials.commands.commands.step;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandStep extends VCommand {

    public CommandStep(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_STEP);
        this.setDescription(Message.DESCRIPTION_STEP);
        this.addSubCommand(new CommandStepStart(plugin));
        this.addSubCommand(new CommandStepFinish(plugin));
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        syntaxMessage();
        return CommandResultType.SUCCESS;
    }
}
