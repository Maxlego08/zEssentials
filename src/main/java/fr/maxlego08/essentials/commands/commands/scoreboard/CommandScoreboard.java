package fr.maxlego08.essentials.commands.commands.scoreboard;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardManager;
import fr.maxlego08.essentials.module.modules.scoreboard.ScoreboardModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;

public class CommandScoreboard extends VCommand {
    public CommandScoreboard(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(ScoreboardModule.class);
        this.setPermission(Permission.ESSENTIALS_SCOREBOARD);
        this.setDescription(Message.DESCRIPTION_SCOREBOARD);
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {

        ScoreboardManager scoreboardManager = plugin.getScoreboardManager();
        scoreboardManager.toggleScoreboard(player, false);

        return CommandResultType.SUCCESS;
    }
}
