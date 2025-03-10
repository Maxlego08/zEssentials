package fr.maxlego08.essentials.zutils.utils;

import fr.maxlego08.essentials.api.block.BlockTracker;
import org.bukkit.block.Block;

public class DefaultBlockTracker implements BlockTracker {

    @Override
    public boolean isTracked(Block block) {
        return false;
    }

    @Override
    public void track(Block block) {

    }
}
