package fr.maxlego08.essentials.api.worldedit;

import org.bukkit.entity.Player;

/**
 * Interface for managing the WorldEdit boss bar that displays progress to the player during operations.
 */
public interface WorldeditBossBar {

    /**
     * Creates and displays a WorldEdit boss bar to the specified player.
     *
     * @param player                     the player to whom the boss bar will be shown
     * @param worldeditBossBarConfiguration the configuration settings for the boss bar
     * @param duration                   the duration in ticks for which the boss bar will be displayed
     */
    void create(Player player, WorldeditBossBarConfiguration worldeditBossBarConfiguration, long duration);

    /**
     * Updates the progress of the WorldEdit boss bar for the specified player.
     *
     * @param player   the player whose boss bar will be updated
     * @param percent  the percentage of progress to display (0.0 to 1.0)
     * @param duration the remaining duration in ticks for which the boss bar will be displayed
     */
    void update(Player player, float percent, long duration);

    /**
     * Removes the WorldEdit boss bar from the specified player's view.
     *
     * @param player the player from whom the boss bar will be removed
     */
    void remove(Player player);
}
