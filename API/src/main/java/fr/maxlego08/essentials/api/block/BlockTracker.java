package fr.maxlego08.essentials.api.block;

import org.bukkit.block.Block;

public interface BlockTracker {

    /**
     * Returns whether the specified block is being tracked.
     *
     * @param block the block to check
     * @return {@code true} if the block is being tracked, {@code false} otherwise
     */
    boolean isTracked(Block block);

    /**
     * Tracks the specified block.
     *
     * @param block the block to be tracked
     */
    void track(Block block);

}
