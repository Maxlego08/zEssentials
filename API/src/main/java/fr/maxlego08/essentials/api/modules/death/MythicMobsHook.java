package fr.maxlego08.essentials.api.modules.death;

import org.bukkit.entity.Entity;

import java.util.Optional;

public interface MythicMobsHook {

    /**
     * Checks if the given entity is a MythicMob.
     *
     * @param entity the entity to check
     * @return true if the entity is a MythicMob, false otherwise
     */
    boolean isMythicMob(Entity entity);

    /**
     * Gets the display name of a MythicMob entity.
     *
     * @param entity the entity to get the name of
     * @return an Optional containing the MythicMob's display name, or empty if not a MythicMob
     */
    Optional<String> getMythicMobName(Entity entity);

}
