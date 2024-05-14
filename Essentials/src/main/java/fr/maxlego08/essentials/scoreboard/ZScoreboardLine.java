package fr.maxlego08.essentials.scoreboard;

import fr.maxlego08.essentials.api.scoreboard.PlayerBoard;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardAnimation;
import fr.maxlego08.essentials.api.scoreboard.ScoreboardLine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ZScoreboardLine implements ScoreboardLine {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final int line;
    private final String text;
    private final ScoreboardAnimation animation;
    private ScheduledFuture<?> future = null;
    private String fromColor;
    private String toColor;

    public ZScoreboardLine(int line, String text) {
        this.line = line;
        this.text = text;
        this.animation = ScoreboardAnimation.NONE;
    }

    public ZScoreboardLine(int line, String text, String fromColor, String toColor) {
        this.line = line;
        this.text = text;
        this.animation = ScoreboardAnimation.COLOR;
        this.fromColor = fromColor;
        this.toColor = toColor;
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
    public ScoreboardAnimation getAnimation() {
        return this.animation;
    }

    @Override
    public void startAnimation(PlayerBoard playerBoard) {

        if (this.animation == ScoreboardAnimation.NONE) return;

        playerBoard.updateLine(this.line, this.fromColor + this.text);
        EXECUTOR_SERVICE.schedule(() -> this.start(playerBoard), 5, TimeUnit.SECONDS);
    }

    private void start(PlayerBoard playerBoard) {

        AtomicInteger atomicInteger = new AtomicInteger(-this.text.length());
        System.out.println("Start at: " + atomicInteger + " -> Finish At: " + (this.text.length()));
        future = EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {

            if (playerBoard.isDeleted()) {
                future.cancel(false);
                return;
            }


            int step = atomicInteger.getAndIncrement();
            playerBoard.updateLine(this.line, getAnimatedText(this.text, this.toColor, this.fromColor, step));

            if (step > this.text.length() * 2) {
                playerBoard.updateLine(this.line, this.fromColor + this.text);
                EXECUTOR_SERVICE.schedule(() -> this.start(playerBoard), 5, TimeUnit.SECONDS);
                future.cancel(false);
            }

        }, 25, 25, TimeUnit.MILLISECONDS);
    }

    private String getAnimatedText(String text, String baseColorHex, String highlightColorHex, int highlightIndex) {
        StringBuilder animatedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String colorHex = blendColors(baseColorHex, highlightColorHex, i, highlightIndex, text.length());
            if (colorHex.length() > 7) {
                colorHex = this.fromColor;
            }
            animatedText.append(colorHex).append(text.charAt(i));
        }
        return animatedText.toString();
    }

    private String blendColors(String baseColorHex, String highlightColorHex, int currentIndex, int highlightIndex, int textLength) {
        int baseColor = Integer.parseInt(baseColorHex.substring(1), 16);
        int highlightColor = Integer.parseInt(highlightColorHex.substring(1), 16);

        float ratio = (float) Math.abs(currentIndex - highlightIndex) / textLength;

        int rBase = (baseColor >> 16) & 0xFF;
        int gBase = (baseColor >> 8) & 0xFF;
        int bBase = baseColor & 0xFF;

        int rHighlight = (highlightColor >> 16) & 0xFF;
        int gHighlight = (highlightColor >> 8) & 0xFF;
        int bHighlight = highlightColor & 0xFF;

        int r = (int) (rBase + ratio * (rHighlight - rBase));
        int g = (int) (gBase + ratio * (gHighlight - gBase));
        int b = (int) (bBase + ratio * (bHighlight - bBase));

        return String.format("#%02x%02x%02x", r, g, b);
    }
}
