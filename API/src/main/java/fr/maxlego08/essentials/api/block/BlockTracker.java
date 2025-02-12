package fr.maxlego08.essentials.api.block;

import org.bukkit.block.Block;

public interface BlockTracker {

    boolean isTracked(Block block);

    void track(Block block);

}
