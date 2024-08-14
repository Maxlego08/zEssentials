package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditAction;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class CutTask extends WorldEditTask {

    public CutTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid) {
        super(plugin, worldeditManager, user, cuboid, List.of(new MaterialPercent(Material.AIR, 100)));
    }

    @Override
    public void loadBlocks() {
        this.blocks = this.cuboid.getBlocks();
    }

    @Override
    public WorldeditAction getAction() {
        return WorldeditAction.CUT;
    }
}
