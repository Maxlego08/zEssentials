package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.api.scoreboard.AnimationConfiguration;
import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimation;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimationType;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;
import fr.maxlego08.essentials.api.scoreboard.configurations.ColorWaveConfiguration;
import fr.maxlego08.essentials.api.scoreboard.configurations.NoneConfiguration;
import fr.maxlego08.essentials.scoreboard.animation.AutoUpdateAnimation;
import fr.maxlego08.essentials.scoreboard.animation.ColorWaveAnimation;
import fr.maxlego08.essentials.zutils.utils.PlaceholderUtils;

public class ZScoreboardLine implements ScoreboardLine {

    private final int configurationLine;
    private final String text;
    private final String eventName;
    private final ScoreboardAnimationType animation;
    private final AnimationConfiguration configuration;
    private int line;

    public ZScoreboardLine(int line, String text, String eventName) {
        this.line = this.configurationLine = line;
        this.text = text;
        this.eventName = eventName;
        this.animation = ScoreboardAnimationType.NONE;
        this.configuration = null;
    }

    public ZScoreboardLine(int line, String text, ScoreboardAnimationType animation, AnimationConfiguration configuration) {
        this.line = this.configurationLine = line;
        this.text = text;
        this.eventName = null;
        this.animation = animation;
        this.configuration = configuration;
    }

    @Override
    public int getLine() {
        return this.line;
    }

    @Override
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int getConfigurationLine() {
        return this.configurationLine;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public ScoreboardAnimationType getAnimation() {
        return this.animation;
    }

    @Override
    public void startAnimation(PlayerBoard playerBoard) {

        ScoreboardAnimation scoreboardAnimation;
        switch (this.animation) {
            case COLOR_WAVE -> {
                ColorWaveConfiguration configuration = (ColorWaveConfiguration) this.configuration;
                scoreboardAnimation = new ColorWaveAnimation(playerBoard, this.text, this.line, configuration);
            }
            default -> {
                if (this.configuration != null) {
                    NoneConfiguration configuration = (NoneConfiguration) this.configuration;
                    scoreboardAnimation = new AutoUpdateAnimation(playerBoard, this.line, this.text, configuration);
                } else scoreboardAnimation = null;
            }
        }

        if (scoreboardAnimation != null) {
            scoreboardAnimation.start();
        }
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public void update(PlayerBoard playerBoard) {
        playerBoard.updateLine(this.line, PlaceholderUtils.PapiHelper.papi(this.text, playerBoard.getPlayer()));
    }
}
