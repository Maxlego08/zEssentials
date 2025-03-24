package fr.maxlego08.essentials.api.scoreboard;

/**
 * Interface representing a line on a scoreboard.
 */
public interface ScoreboardLine {

    /**
     * Gets the line number of this scoreboard line.
     *
     * @return the line number
     */
    int getLine();

    /**
     * Gets the text of this scoreboard line.
     *
     * @return the text of the line
     */
    String getText();

    /**
     * Gets the animation type associated with this scoreboard line.
     *
     * @return the scoreboard animation type
     */
    ScoreboardAnimationType getAnimation();

    /**
     * Starts the animation for this scoreboard line on the specified player board.
     *
     * @param playerBoard the player board on which to start the animation
     */
    void startAnimation(PlayerBoard playerBoard);

    /**
     * Gets the event name associated with this scoreboard line.
     *
     * @return the event name
     */
    String getEventName();

    /**
     * Updates this scoreboard line on the specified player board.
     *
     * @param board the player board to update
     */
    void update(PlayerBoard board);
}
