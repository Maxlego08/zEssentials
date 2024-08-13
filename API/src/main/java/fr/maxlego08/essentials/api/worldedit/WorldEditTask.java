package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class WorldEditTask {

    private final EssentialsPlugin plugin;
    private final WorldeditManager worldeditManager;
    private final User user;
    private final List<Block> blocks;
    private final List<MaterialPercent> materialPercents;
    private final List<BlockInfo> blockInfos = new ArrayList<>();
    private final Random random = new Random();
    private WorldeditStatus worldeditStatus = WorldeditStatus.NOTHING;

    public WorldEditTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, List<Block> blocks, List<MaterialPercent> materialPercents) {
        this.plugin = plugin;
        this.worldeditManager = worldeditManager;
        this.user = user;
        this.blocks = blocks;
        this.materialPercents = materialPercents;
    }

    public void calculatePrice(Consumer<BigDecimal> consumer) {
        this.worldeditStatus = WorldeditStatus.CALCULATE_PRICE;
        processNextBatch(blocks, 0, 100, () -> {

            System.out.println(this.blockInfos.size());

            BigDecimal bigDecimal = this.blockInfos.stream().map(BlockInfo::price).reduce(BigDecimal.ZERO, BigDecimal::add);

            this.worldeditStatus = WorldeditStatus.WAITING_RESPONSE_PRICE;
            consumer.accept(bigDecimal);
        }, blocks -> blocks.forEach(this::processBlock));
    }

    private void processNextBatch(List<Block> blocks, int startIndex, int batchSize, Runnable runnable, Consumer<List<Block>> consumer) {

        if (startIndex >= blocks.size()) {
            runnable.run();
            return;
        }

        int endIndex = Math.min(startIndex + batchSize, blocks.size());
        List<Block> currentBatch = blocks.subList(startIndex, endIndex);

        if (currentBatch.isEmpty()) {
            runnable.run();
            return;
        }

        var firstBlock = currentBatch.get(0);

        var scheduler = this.plugin.getScheduler();
        scheduler.runAtLocation(firstBlock.getLocation(), wrappedTask -> {
            consumer.accept(currentBatch);
            scheduler.runNextTick(wt -> processNextBatch(blocks, endIndex, batchSize, runnable, consumer));
        });
    }

    private void processBlock(Block block) {

        var randomMaterial = selectRandomMaterial();
        BigDecimal blockPrice = this.worldeditManager.getMaterialPrice(randomMaterial);

        System.out.println(blockPrice + " - " + randomMaterial);

        this.blockInfos.add(new BlockInfo(block, randomMaterial, blockPrice));
    }

    public abstract void process();

    public Material selectRandomMaterial() {

        if (this.materialPercents.size() == 1) return this.materialPercents.get(0).material();

        double randomValue = random.nextDouble();
        double cumulativePercent = 0.0;

        for (MaterialPercent mp : this.materialPercents) {
            cumulativePercent += mp.percent();
            if (randomValue <= cumulativePercent) {
                return mp.material();
            }
        }

        return selectRandomMaterial();
    }

    public WorldeditStatus getWorldeditStatus() {
        return worldeditStatus;
    }

    public void confirm(Consumer<Boolean> consumer) {

        this.worldeditStatus = WorldeditStatus.CHECK_INVENTORY_CONTENT;

        // ToDo

        this.blockInfos.forEach(blockInfo -> blockInfo.block().setType(blockInfo.newMaterial()));

        consumer.accept(true);
    }
}
