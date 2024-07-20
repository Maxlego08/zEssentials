package fr.maxlego08.essentials.commands.commands.utils;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandPlayTime extends VCommand {
    public CommandPlayTime(EssentialsPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ESSENTIALS_PLAY_TIME);
        this.setDescription(Message.DESCRIPTION_PLAY_TIME);
        this.onlyPlayers();
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        message(sender, Message.COMMAND_PLAY_TIME,
                "%playtime%", TimerBuilder.getStringTime(this.user.getPlayTime() * 1000),
                "%playtime_session%", TimerBuilder.getStringTime(System.currentTimeMillis() - this.user.getCurrentSessionPlayTime())
        );
        return CommandResultType.SUCCESS;
    }
}
