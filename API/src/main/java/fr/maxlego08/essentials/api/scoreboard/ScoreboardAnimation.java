package fr.maxlego08.essentials.api.scoreboard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Abstract class representing a scoreboard animation.
 */
public abstract class ScoreboardAnimation {

    /**
     * A single-threaded executor service for running scheduled tasks.
     */
    protected static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    /**
     * The player board associated with this animation.
     */
    protected final PlayerBoard playerBoard;

    /**
     * A future representing the scheduled task for the animation.
     */
    protected ScheduledFuture<?> future = null;

    /**
     * Constructs a new ScoreboardAnimation for the given player board.
     *
     * @param playerBoard the player board to associate with this animation
     */
    public ScoreboardAnimation(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Starts the scoreboard animation.
     */
    public abstract void start();

    /**
     * Blends two colors based on the current position in the animation.
     *
     * @param baseColorHex      the base color in hex format (e.g., "#ffffff")
     * @param highlightColorHex the highlight color in hex format (e.g., "#ff0000")
     * @param currentIndex      the current index in the text
     * @param highlightIndex    the index of the highlight
     * @param textLength        the total length of the text
     * @return the blended color as a hex string (e.g., "#ffaa00")
     */
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
