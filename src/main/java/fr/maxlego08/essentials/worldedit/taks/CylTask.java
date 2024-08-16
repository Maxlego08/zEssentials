package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditAction;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class CylTask extends WorldEditTask {

    private final int radius;
    private final int height;
    private final boolean filled;

    public CylTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid, List<MaterialPercent> materialPercents, int radius, int height, boolean filled) {
        super(plugin, worldeditManager, user, cuboid, materialPercents);
        this.radius = radius;
        this.height = height;
        this.filled = filled;
    }

    @Override
    public void loadBlocks() {

        Location centerLocation = user.getSelection().getFirstLocation();
        Block centerBlock = centerLocation.getBlock();

        int ceilRadius = (int) (double) radius;
        for (int y = 0; y < height; y++) {
            for (int x = -ceilRadius; x <= ceilRadius; x++) {
                for (int z = -ceilRadius; z <= ceilRadius; z++) {
                    if (x * x + z * z < radius * radius) {
                        if (!filled) {
                            if (lengthSq(x, z) <= (radius * radius) - (radius * 2)) {
                                continue;
                            }
                        }
                        this.blocks.add(centerBlock.getRelative(x, y, z));
                    }

                }
            }
        }

    }

    @Override
    public WorldeditAction getAction() {
        return WorldeditAction.PLACE;
    }
}
