package fr.maxlego08.essentials.api.utils;

import java.util.Optional;
import java.util.UUID;

/**
 * A utility for managing random words.
 */
public interface RandomWord {

    /**
     * Clears all the random words.
     */
    void clear();

    /**
     * Gets the random word for the given uuid.
     *
     * @param uuid the uuid of the player.
     * @return the random word for the given uuid.
     */
    Optional<String> get(UUID uuid);

    /**
     * Sets the random word for the given uuid.
     *
     * @param uuid  the uuid of the player.
     * @param word  the random word.
     */
    void set(UUID uuid, String word);

}
