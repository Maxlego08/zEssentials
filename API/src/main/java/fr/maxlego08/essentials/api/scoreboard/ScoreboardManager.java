package fr.maxlego08.essentials.api.scoreboard;

import fr.maxlego08.essentials.api.modules.Module;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * Interface representing the manager for handling scoreboards in the game.
 * This interface provides methods to manage and interact with different scoreboards.
 */
public interface ScoreboardManager extends Module {

    /**
     * Retrieves all available EssentialsScoreboards.
     *
     * @return a list of all EssentialsScoreboards
     */
    List<EssentialsScoreboard> getEssentialsScoreboard();

    /**
     * Retrieves the default EssentialsScoreboard.
     *
     * @return the default EssentialsScoreboard
     */
    EssentialsScoreboard getDefaultScoreboard();

    /**
     * Creates a new PlayerBoard for the specified player with the provided EssentialsScoreboard.
     *
     * @param player the player for whom the scoreboard is to be created
     * @param essentialsScoreboard the EssentialsScoreboard to use
     * @return the created PlayerBoard
     */
    PlayerBoard createScoreboard(Player player, EssentialsScoreboard essentialsScoreboard);

    /**
     * Deletes the scoreboard associated with the specified player.
     *
     * @param player the player whose scoreboard is to be deleted
     */
    void deleteBoard(Player player);

    /**
     * Retrieves the PlayerBoard associated with the specified player, if it exists.
     *
     * @param player the player whose PlayerBoard is to be retrieved
     * @return an Optional containing the PlayerBoard if present, otherwise an empty Optional
     */
    Optional<PlayerBoard> getBoard(Player player);

    /**
     * Reloads the scoreboards for all players.
     */
    void reloadPlayers();

    /**
     * Retrieves the EssentialsScoreboard by its name, if it exists.
     *
     * @param name the name of the scoreboard
     * @return an Optional containing the EssentialsScoreboard if present, otherwise an empty Optional
     */
    Optional<EssentialsScoreboard> getScoreboard(String name);

    /**
     * Toggles the visibility of the scoreboard for the specified player.
     *
     * @param player the player for whom the scoreboard visibility is toggled
     * @param silent if true, the toggle action will not send any messages to the player
     */
    void toggleScoreboard(Player player, boolean silent);

    /**
     * Retrieves the EssentialsScoreboard to be shown to the player when they join.
     *
     * @param player the player joining the game
     * @return the EssentialsScoreboard to display on join
     */
    EssentialsScoreboard getJoinScoreboard(Player player);

    /**
     * Retrieves the EssentialsScoreboard associated with tasks for the specified player.
     *
     * @param player the player whose task scoreboard is to be retrieved
     * @return the EssentialsScoreboard associated with tasks
     */
    EssentialsScoreboard getTaskScoreboard(Player player);
}
