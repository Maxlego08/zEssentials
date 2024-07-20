package fr.maxlego08.essentials.api.utils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a cache for player names.
 * This class provides methods to add, remove, get, and set player names in the cache.
 */
public class PlayerCache {

    // Set to store player names, thread-safe using ConcurrentHashMap
    private final Set<String> players = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Adds a player name to the cache.
     *
     * @param playerName The name of the player to add.
     */
    public void addPlayer(String playerName) {
        players.add(playerName);
    }

    /**
     * Removes a player name from the cache.
     *
     * @param playerName The name of the player to remove.
     */
    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    /**
     * Gets the set of player names stored in the cache.
     *
     * @return The set of player names.
     */
    public Set<String> getPlayers() {
        return players;
    }

    /**
     * Sets the player names in the cache, replacing any existing names.
     *
     * @param players The set of player names to set.
     */
    public void setPlayers(Set<String> players) {
        // Clear the existing set and add all players from the given set
        this.players.clear();
        this.players.addAll(players);
    }
}

