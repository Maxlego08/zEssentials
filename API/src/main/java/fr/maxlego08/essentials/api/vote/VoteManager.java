package fr.maxlego08.essentials.api.vote;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Interface for managing votes and vote-related actions within a Minecraft server.
 * It includes methods for tracking votes, handling vote parties, and managing voting sites.
 */
public interface VoteManager {

    /**
     * Gets the current amount of votes towards the next vote party.
     *
     * @return the current vote party amount
     */
    long getCurrentVotePartyAmount();

    /**
     * Sets the current amount of votes towards the next vote party.
     *
     * @param amount the new vote party amount
     */
    void setCurrentVotePartyAmount(long amount);

    /**
     * Adds a specified amount to the current vote party count.
     *
     * @param amount the amount to add
     */
    void addCurrentVotePartyAmount(long amount);

    /**
     * Removes a specified amount from the current vote party count.
     *
     * @param amount the amount to remove
     */
    void removeCurrentVotePartyAmount(long amount);

    /**
     * Handles actions to be taken when a player votes.
     *
     * @param uniqueId the UUID of the player who voted
     * @param site     the name of the voting site
     */
    void onPlayerVote(UUID uniqueId, String site);

    /**
     * Adds a vote for a player from a specified site.
     *
     * @param offlinePlayer the player who voted
     * @param site          the name of the voting site
     */
    void addPlayerVote(OfflinePlayer offlinePlayer, String site);

    /**
     * Handles the logic for vote parties when a certain vote amount is reached.
     *
     * @param amount the amount of votes contributing to the vote party
     */
    void handleVoteParty(long amount);

    /**
     * Gets the objective amount of votes needed to trigger a vote party.
     *
     * @return the vote party objective
     */
    long getVotePartyObjective();

    /**
     * Opens the vote inventory GUI for a player.
     *
     * @param player the player to open the vote inventory for
     */
    void openVoteInventory(Player player);

    /**
     * Sends a vote party notification or message to a player.
     *
     * @param player the player to send the vote party message to
     */
    void sendVoteParty(Player player);

    /**
     * Checks if a specified voting site exists.
     *
     * @param site the name of the voting site
     * @return true if the site exists, false otherwise
     */
    boolean siteExist(String site);

    /**
     * Gets the list of all configured voting sites.
     *
     * @return a list of {@link VoteSiteConfiguration} objects representing the voting sites
     */
    List<VoteSiteConfiguration> getSites();

    /**
     * Gets the cooldown period for voting on a specified site.
     *
     * @param site the name of the voting site
     * @return the cooldown period in milliseconds
     */
    long getSiteCooldown(String site);

    /**
     * Gets the placeholder text for displaying available votes.
     *
     * @return the placeholder text for available votes
     */
    String getPlaceholderAvailable();

    /**
     * Gets the placeholder text for displaying vote cooldowns.
     *
     * @return the placeholder text for vote cooldowns
     */
    String getPlaceholderCooldown();

    /**
     * Starts the task responsible for resetting votes periodically.
     */
    void startResetTask();

    /**
     * Resets the vote counts, typically done at the end of a voting period.
     */
    void resetVotes();
}
