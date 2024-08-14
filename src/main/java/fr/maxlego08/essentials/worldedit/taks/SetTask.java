package fr.maxlego08.essentials.worldedit.taks;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.BlockInfo;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SetTask extends WorldEditTask {


    public SetTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, List<Block> blocks, List<MaterialPercent> materialPercents) {
        super(plugin, worldeditManager, user, blocks, materialPercents);
    }

    @Override
    public void process() {

        var scheduler = this.plugin.getScheduler();

        int blocksPerSecond = 15; // ToDo, dans la config

        int intervalTicks;
        int blocksPerTick;

        if (blocksPerSecond <= 20) {
            // Si on place moins ou exactement 20 blocs par seconde, calculer l'intervalle
            intervalTicks = 20 / blocksPerSecond;
            blocksPerTick = 1;
        } else {
            // Si on place plus de 20 blocs par seconde, on place plusieurs blocs par tick
            intervalTicks = 1;
            blocksPerTick = blocksPerSecond / 20;
        }

        AtomicInteger tickCounter = new AtomicInteger();
        int additionalBlocks = blocksPerSecond % 20;

        scheduler.runTimerAsync(wrappedTask -> {

            if (blockInfos.isEmpty()) {
                wrappedTask.cancel();
                finish();
                return;
            }

            // Placer les blocs par tick
            for (int i = 0; i < blocksPerTick; i++) {
                if (!blockInfos.isEmpty()) {
                    placeBlock(blockInfos.poll());
                }
            }

            // Gérer les blocs supplémentaires à placer lorsque blocksPerSecond n'est pas divisible par 20
            if (blocksPerSecond > 20 && tickCounter.get() < additionalBlocks) {
                if (!blockInfos.isEmpty()) {
                    placeBlock(blockInfos.poll());
                }
            }

            tickCounter.getAndIncrement();

        }, 0L, intervalTicks);
    }

    private void placeBlock(BlockInfo blockInfo) {
        if (blockInfo != null) {

            var scheduler = this.plugin.getScheduler();
            var block = blockInfo.block();

            scheduler.runAtLocation(block.getLocation(), wrappedTask -> {

                var currentType = block.getType();
                if (worldeditManager.isBlacklist(currentType)) return;

                if (!currentType.isAir()) {
                    add(currentType);
                }

                block.setType(blockInfo.newMaterial());
            });
        }
    }
}
