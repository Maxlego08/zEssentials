package fr.maxlego08.essentials.api.scoreboard;

import java.util.List;

/**
 * Represents a scoreboard in the Essentials system, which can be displayed to players.
 */
public interface EssentialsScoreboard {

    /**
     * Gets the name of the scoreboard.
     *
     * @return the name of the scoreboard
     */
    String getName();

    /**
     * Checks if this scoreboard is the default scoreboard.
     *
     * @return true if this is the default scoreboard, false otherwise
     */
    boolean isDefault();

    /**
     * Gets the title of the scoreboard.
     *
     * @return the title of the scoreboard
     */
    String getTitle();

    /**
     * Gets the lines of text that make up the scoreboard.
     *
     * @return a list of {@link ScoreboardLine} representing the lines of the scoreboard
     */
    List<ScoreboardLine> getLines();

    /**
     * Creates the scoreboard for a specific player.
     *
     * @param playerBoard the {@link PlayerBoard} representing the player's scoreboard
     */
    void create(PlayerBoard playerBoard);

    /**
     * Updates the scoreboard for a specific player.
     *
     * @param playerBoard the {@link PlayerBoard} representing the player's scoreboard
     */
    void update(PlayerBoard playerBoard);

    /**
     * Updates the scoreboard for a specific player when a specific event is triggered.
     * This can be used to update the scoreboard when a player joins or leaves the server,
     * or when a player completes a task.
     *
     * @param playerBoard the {@link PlayerBoard} representing the player's scoreboard
     * @param eventName   the name of the event that triggered the update
     */
    void update(PlayerBoard playerBoard, String eventName);
}
