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

public class ZScoreboardLine implements ScoreboardLine {
    private final int line;
    private final String text;
    private final ScoreboardAnimationType animation;
    private final AnimationConfiguration configuration;

    public ZScoreboardLine(int line, String text) {
        this.line = line;
        this.text = text;
        this.animation = ScoreboardAnimationType.NONE;
        this.configuration = null;
    }

    public ZScoreboardLine(int line, String text, ScoreboardAnimationType animation, AnimationConfiguration configuration) {
        this.line = line;
        this.text = text;
        this.animation = animation;
        this.configuration = configuration;
    }

    @Override
    public int getLine() {
        return this.line;
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
}
