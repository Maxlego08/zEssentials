package fr.maxlego08.essentials.nms.v1_21_5.enderchest;

import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Based on: <a href="https://github.com/Jikoo/OpenInv/tree/master">OpenInv</a>
 */
public class CraftPlayerManager implements fr.maxlego08.essentials.api.nms.PlayerManager {


    @Override
    public @Nullable Player loadPlayer(@NotNull OfflinePlayer offline) {
        throw new NotImplementedException();
    }

    @Override
    public @NotNull Player inject(@NotNull Player player) {
        throw new NotImplementedException();
    }
}
