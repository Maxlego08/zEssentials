package fr.maxlego08.essentials.api.economy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the balance top list functionality.
 */
public interface Baltop {

    /**
     * Retrieves the economy instance associated with the balance top list.
     *
     * @return the Economy instance
     */
    Economy getEconomy();

    /**
     * Retrieves the user at the specified position in the balance top list.
     *
     * @param position the position in the balance top list
     * @return an Optional containing the UserBaltop if found, otherwise an empty Optional
     */
    Optional<UserBaltop> getPosition(int position);

    /**
     * Retrieves the position of the user with the specified UUID in the balance top list.
     *
     * @param uuid the UUID of the user
     * @return the position of the user as a long
     */
    long getUserPosition(UUID uuid);

    /**
     * Retrieves the creation time of the balance top list.
     *
     * @return the creation time as a long value representing milliseconds since epoch
     */
    long getCreatedAt();

    List<UserBaltop> getUsers();
}
