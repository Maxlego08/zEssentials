package fr.maxlego08.essentials.scoreboard.animation;

import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimation;
import fr.maxlego08.essentials.api.scoreboard.configurations.NoneConfiguration;
import fr.maxlego08.essentials.zutils.utils.PlaceholderUtils;

import java.util.concurrent.TimeUnit;

public class AutoUpdateAnimation extends ScoreboardAnimation {

    private final int line;
    private final String text;
    private final NoneConfiguration configuration;

    public AutoUpdateAnimation(PlayerBoard playerBoard, int line, String text, NoneConfiguration configuration) {
        super(playerBoard);
        this.line = line;
        this.text = text;
        this.configuration = configuration;
    }

    @Override
    public void start() {
        future = EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {

            if (playerBoard.isDeleted()) {
                future.cancel(false);
                return;
            }

            playerBoard.updateLine(this.line, PlaceholderUtils.PapiHelper.papi(this.text, playerBoard.getPlayer()));

        }, this.configuration.update(), this.configuration.update(), TimeUnit.MILLISECONDS);
    }
}
