package fr.maxlego08.essentials.commands.commands.fly;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandFlyInfo extends VCommand {
    public CommandFlyInfo(EssentialsPlugin plugin) {
        super(plugin);
        this.addSubCommand("info");
        this.setPermission(Permission.ESSENTIALS_FLY_INFO);
        this.setDescription(Message.DESCRIPTION_FLY_INFO);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        long flySeconds = user.getFlySeconds();

        if (flySeconds <= 0) {
            message(sender, Message.COMMAND_FLY_INFO_EMPTY);
        } else {
            message(sender, Message.COMMAND_FLY_INFO, "%time%", TimerBuilder.getStringTime(flySeconds * 1000));
        }

        return CommandResultType.SUCCESS;
    }
}
