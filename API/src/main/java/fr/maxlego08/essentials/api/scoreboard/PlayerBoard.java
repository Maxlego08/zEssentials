package fr.maxlego08.essentials.api.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerBoard {

    /**
     * Update the scoreboard username.
     *
     * @param title the new scoreboard username
     * @throws IllegalArgumentException if the username is longer than 32 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    void updateTitle(String title);

    /**
     * Update a single scoreboard line.
     *
     * @param line the line number
     * @param text the new line text
     * @throws IndexOutOfBoundsException if the line is higher than {@link #size() size() + 1}
     */
    void updateLine(int line, String text);

    /**
     * Update all the scoreboard lines.
     *
     * @param lines the new lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    void updateLines(String... lines);

    /**
     * Remove a scoreboard line.
     *
     * @param line the line number
     */
    void removeLine(int line);

    /**
     * Get the player who has the scoreboard.
     *
     * @return current player for this FastBoard
     */
    Player getPlayer();

    /**
     * Get the scoreboard id.
     *
     * @return the id
     */
    String getId();


    /**
     * Get if the scoreboard is deleted.
     *
     * @return true if the scoreboard is deleted
     */
    boolean isDeleted();

    /**
     * Get if the server supports custom scoreboard scores (1.20.3+ servers only).
     *
     * @return true if the server supports custom scores
     */
    boolean customScoresSupported();

    /**
     * Get the scoreboard size (the number of lines).
     *
     * @return the size
     */
    int size();

    /**
     * Delete this FastBoard, and will remove the scoreboard for the associated player if he is online.
     * After this, all uses of {@link #updateLines} and {@link #updateTitle} will throw an {@link IllegalStateException}
     *
     * @throws IllegalStateException if this was already call before
     */
    void delete();

    /**
     * Updates the lines of text on the scoreboard.
     *
     * @param lines a list of strings representing the new lines to display on the scoreboard
     */
    void updateLines(List<String> lines);

    /**
     * Gets the associated EssentialsScoreboard.
     *
     * @return the {@link EssentialsScoreboard} instance
     */
    EssentialsScoreboard getScoreboard();
}
