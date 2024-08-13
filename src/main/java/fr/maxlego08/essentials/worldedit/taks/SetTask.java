package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import org.bukkit.block.Block;

import java.util.List;

public class SetTask extends WorldEditTask {


    public SetTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, List<Block> blocks, List<MaterialPercent> materialPercents) {
        super(plugin, worldeditManager, user, blocks, materialPercents);
    }

    @Override
    public void process() {

    }
}
