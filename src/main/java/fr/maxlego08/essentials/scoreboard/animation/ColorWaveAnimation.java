package fr.maxlego08.essentials.scoreboard.animation;

import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimation;
import fr.maxlego08.essentials.api.scoreboard.configurations.ColorWaveConfiguration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ColorWaveAnimation extends ScoreboardAnimation {

    private final String text;
    private final ColorWaveConfiguration configuration;

    public ColorWaveAnimation(PlayerBoard playerBoard, String text, int line, ColorWaveConfiguration configuration) {
        super(playerBoard, line);
        this.text = text;
        this.configuration = configuration;
    }

    @Override
    public void start() {
        playerBoard.updateLine(this.line, this.configuration.fromColor() + this.text);
        EXECUTOR_SERVICE.schedule(this::animation, this.configuration.delayBetween(), TimeUnit.MILLISECONDS);
    }

    private void animation() {

        if (playerBoard.isDeleted()) return;

        AtomicInteger atomicInteger = new AtomicInteger(-this.configuration.length());
        future = EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {

            if (playerBoard.isDeleted()) {
                future.cancel(false);
                return;
            }


            int step = atomicInteger.getAndIncrement();
            playerBoard.updateLine(this.line, getAnimatedText(this.text, this.configuration.toColor(), this.configuration.fromColor(), step));

            if (step > this.text.length() + this.configuration.length()) {
                playerBoard.updateLine(this.line, this.configuration.fromColor() + this.text);
                EXECUTOR_SERVICE.schedule(this::animation, this.configuration.delayBetween(), TimeUnit.MILLISECONDS);
                future.cancel(false);
            }

        }, 0, this.configuration.animationSpeed(), TimeUnit.MILLISECONDS);
    }

    private String getAnimatedText(String text, String baseColorHex, String highlightColorHex, int highlightIndex) {
        StringBuilder animatedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {

            String colorHex = blendColors(baseColorHex, highlightColorHex, i, highlightIndex, this.configuration.length());

            if (colorHex.length() > 7) {
                colorHex = this.configuration.fromColor();
            }

            animatedText.append(colorHex).append(text.charAt(i));
        }
        return animatedText.toString();
    }

}
