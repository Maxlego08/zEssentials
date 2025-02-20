package fr.maxlego08.essentials.commands.commands.step;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.steps.Step;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandStep extends VCommand {

    public CommandStep(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_STEP);
        this.setDescription(Message.DESCRIPTION_STEP);
        this.addRequirePlayerNameArg();
        this.addRequireArg("step", (a, b) -> this.plugin.getStepManager().getSteps().stream().map(Step::name).toList());
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String stepName = this.argAsString(1);
        var manager = this.plugin.getStepManager();

        var optional = manager.getStep(stepName);
        if (optional.isEmpty()) {
            message(sender, Message.STEP_DOESNT_EXIST, "%step%", stepName);
            return CommandResultType.SYNTAX_ERROR;
        }
        var step = optional.get();

        manager.handleStep(sender, player, step);

        return CommandResultType.SUCCESS;
    }
}
