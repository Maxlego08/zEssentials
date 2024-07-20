package fr.maxlego08.essentials.api.scoreboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public abstract class ScoreboardAnimation {

    protected static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    protected final PlayerBoard playerBoard;
    protected ScheduledFuture<?> future = null;

    public ScoreboardAnimation(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    public abstract void start();

    protected String blendColors(String baseColorHex, String highlightColorHex, int currentIndex, int highlightIndex, int textLength) {
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
