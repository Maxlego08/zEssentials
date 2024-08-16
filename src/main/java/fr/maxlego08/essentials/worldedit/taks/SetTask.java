package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.Cuboid;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditAction;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;

import java.util.List;

public class SetTask extends WorldEditTask {

    public SetTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid, List<MaterialPercent> materialPercents) {
        super(plugin, worldeditManager, user, cuboid, materialPercents);
    }

    @Override
    public void loadBlocks() {
        this.blocks = this.cuboid.getBlocks();
    }

    @Override
    public WorldeditAction getAction() {
        return WorldeditAction.PLACE;
    }
}
