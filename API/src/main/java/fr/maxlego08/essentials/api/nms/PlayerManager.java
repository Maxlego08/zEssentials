package fr.maxlego08.essentials.api.nms;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerManager {

    /**
     * Loads a Player for an OfflinePlayer.
     * </p>
     * This method is potentially blocking, and should not be called on the main thread.
     *
     * @param offline the OfflinePlayer
     * @return the Player loaded
     */
    @Nullable Player loadPlayer(@NotNull OfflinePlayer offline);

    /**
     * Creates a new Player from an existing one that will function slightly better offline.
     *
     * @return the Player
     */
    @NotNull Player inject(@NotNull Player player);

}