package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditAction;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class WallsTask extends WorldEditTask {

    public WallsTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid, List<MaterialPercent> materialPercents) {
        super(plugin, worldeditManager, user, cuboid, materialPercents);
    }

    @Override
    public void loadBlocks() {

        int minX = this.cuboid.getLowerX();
        int maxX = this.cuboid.getUpperX();
        int minY = this.cuboid.getLowerY();
        int maxY = this.cuboid.getUpperY();
        int minZ = this.cuboid.getLowerZ();
        int maxZ = this.cuboid.getUpperZ();

        World w = this.cuboid.getWorld();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                this.blocks.add(new Location(w, x, y, minZ).getBlock());
                this.blocks.add(new Location(w, x, y, maxZ).getBlock());
            }
        }

        for (int z = minZ + 1; z < maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                this.blocks.add(new Location(w, minX, y, z).getBlock());
                this.blocks.add(new Location(w, maxX, y, z).getBlock());
            }
        }
    }

    @Override
    public WorldeditAction getAction() {
        return WorldeditAction.PLACE;
    }
}
