package fr.maxlego08.essentials.api.afk;

import fr.maxlego08.essentials.api.user.User;
import org.bukkit.permissions.Permissible;

import java.util.Optional;

public interface AfkManager {

    /**
     * Checks the AFK status of the specified user and updates it if necessary.
     *
     * @param user The user whose AFK status is to be checked.
     */
    void checkUser(User user);

    /**
     * Retrieves the AFK permission for the given permissible entity.
     *
     * @param permissible The permissible entity (e.g., player or command sender).
     * @return An Optional containing the AFK permission if found, or empty otherwise.
     */
    Optional<AfkPermission> getPermission(Permissible permissible);

    /**
     * Returns the configured placeholder that should be displayed when a player is AFK.
     *
     * @return the AFK placeholder string.
     */
    String getPlaceholderAfk();

    /**
     * Returns the configured placeholder that should be displayed when a player is not AFK.
     *
     * @return the non-AFK placeholder string.
     */
    String getPlaceholderNotAfk();

}
