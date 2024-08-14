package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class WorldEditTask {

    protected final EssentialsPlugin plugin;
    protected final WorldeditManager worldeditManager;
    protected final User user;
    protected final Cuboid cuboid;
    protected final List<MaterialPercent> materialPercents;
    protected final Queue<BlockInfo> blockInfos = new LinkedList<>();
    protected final Random random = new Random();
    protected final List<WorldeditRule> worldeditRules = new ArrayList<>();
    protected WorldeditStatus worldeditStatus = WorldeditStatus.NOTHING;
    protected Map<Material, Long> materials = new HashMap<>();
    protected Map<Material, Long> needToGiveMaterials = new HashMap<>();
    protected List<Block> blocks;
    protected BigDecimal totalPrice;

    public WorldEditTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid, List<MaterialPercent> materialPercents) {
        this.plugin = plugin;
        this.worldeditManager = worldeditManager;
        this.user = user;
        this.cuboid = cuboid;
        this.materialPercents = materialPercents;
    }

    public abstract void loadBlocks();

    public abstract WorldeditAction getAction();

    protected void addRules(WorldeditRule... worldeditRules) {
        this.worldeditRules.addAll(Arrays.asList(worldeditRules));
    }

    public void calculatePrice(Consumer<BigDecimal> consumer) {
        this.worldeditStatus = WorldeditStatus.CALCULATE_PRICE;

        this.plugin.getScheduler().runAsync(wrappedTask -> {

            // Calculate the list of blocks that need to be modified
            this.loadBlocks();

            processNextBatch(blocks.stream().map(b -> new BlockInfo(b, null, BigDecimal.ZERO)).collect(Collectors.toList()), 0, 100, () -> {

                this.totalPrice = this.blockInfos.stream().map(BlockInfo::price).reduce(BigDecimal.ZERO, BigDecimal::add);
                this.materials = this.blockInfos.stream().collect(Collectors.groupingBy(BlockInfo::newMaterial, Collectors.counting()));
                this.worldeditStatus = WorldeditStatus.WAITING_RESPONSE_PRICE;

                consumer.accept(totalPrice);
            }, blocks -> blocks.forEach(block -> this.processBlock(block.block())));
        });
    }

    protected void processNextBatch(List<BlockInfo> blocks, int startIndex, int batchSize, Runnable runnable, Consumer<List<BlockInfo>> consumer) {

        if (startIndex >= blocks.size()) {
            runnable.run();
            return;
        }

        int endIndex = Math.min(startIndex + batchSize, blocks.size());
        List<BlockInfo> currentBatch = blocks.subList(startIndex, endIndex);

        if (currentBatch.isEmpty()) {
            runnable.run();
            return;
        }

        var firstBlock = currentBatch.get(0);

        var scheduler = this.plugin.getScheduler();
        scheduler.runAtLocation(firstBlock.block().getLocation(), wrappedTask -> {
            consumer.accept(currentBatch);
            scheduler.runNextTick(wt -> processNextBatch(blocks, endIndex, batchSize, runnable, consumer));
        });
    }

    private void processBlock(Block block) {

        var randomMaterial = selectRandomMaterial();

        if (block.getType() == randomMaterial || worldeditManager.isBlacklist(block.getType())) return;

        for (WorldeditRule worldeditRule : this.worldeditRules) {
            if (worldeditRule == WorldeditRule.ONLY_AIR) {
                if (!block.getType().isAir()) {
                    return;
                }
            }
        }

        BigDecimal blockPrice = this.worldeditManager.getMaterialPrice(randomMaterial);
        this.blockInfos.add(new BlockInfo(block, randomMaterial, blockPrice));
    }

    public Material selectRandomMaterial() {

        if (this.materialPercents.size() == 1) return this.materialPercents.get(0).material();

        double randomValue = random.nextDouble() * 100.0;
        double cumulativePercent = 0.0;

        for (MaterialPercent materialPercent : this.materialPercents) {
            cumulativePercent += materialPercent.percent();
            if (randomValue <= cumulativePercent) {
                return materialPercent.material();
            }
        }

        return this.materialPercents.get(this.materialPercents.size() - 1).material();
    }

    public WorldeditStatus getWorldeditStatus() {
        return worldeditStatus;
    }

    public void confirm(Consumer<Boolean> consumer) {

        this.worldeditStatus = WorldeditStatus.CHECK_INVENTORY_CONTENT;

        this.plugin.getScheduler().runAsync(wrappedTask -> {

            Player player = user.getPlayer();
            boolean result;

            if (getAction() == WorldeditAction.PLACE) {
                result = hasRequiredItems(player, materials);
                if (!result) this.worldeditStatus = WorldeditStatus.NOT_ENOUGH_ITEMS;
            } else {
                result = true;
            }

            plugin.getScheduler().runNextTick(wrappedTask1 -> consumer.accept(result));
        });
    }

    public void startPlaceBlocks() {

        this.worldeditStatus = WorldeditStatus.RUNNING;
        Player player = user.getPlayer();

        this.removeRequiredItems(player, this.materials);

        this.process();
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    private boolean hasRequiredItems(Player player, Map<Material, Long> requiredItems) {
        Inventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        for (Map.Entry<Material, Long> entry : requiredItems.entrySet()) {
            Material material = entry.getKey();
            long requiredAmount = entry.getValue();

            long totalAmount = 0;
            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.getType() == material) {
                    totalAmount += item.getAmount();
                }
            }

            if (totalAmount < requiredAmount) {
                long amountInVault = vaultManager.getMaterialAmount(player, material);
                totalAmount += amountInVault;

                if (totalAmount < requiredAmount) {
                    return false;
                }
            }
        }

        return true;
    }

    public void removeRequiredItems(Player player, Map<Material, Long> requiredItems) {
        Inventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        // Vérifier d'abord si le joueur a bien tous les items nécessaires
        if (!hasRequiredItems(player, requiredItems)) {
            return;
        }

        // Retirer les items de l'inventaire et du vault si nécessaire
        for (Map.Entry<Material, Long> entry : requiredItems.entrySet()) {
            Material material = entry.getKey();
            long amountToRemove = entry.getValue();

            // Retirer de l'inventaire d'abord
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() == material) {
                    long itemAmount = item.getAmount();

                    if (itemAmount > amountToRemove) {
                        item.setAmount((int) (itemAmount - amountToRemove));
                        amountToRemove = 0;
                        break;
                    } else {
                        amountToRemove -= itemAmount;
                        inventory.clear(i);
                    }

                    if (amountToRemove == 0) {
                        break;
                    }
                }
            }

            // Si on doit encore retirer des items, retirer du vault
            if (amountToRemove > 0) {
                vaultManager.removeMaterial(player, material, amountToRemove);
            }
        }

    }


    protected void finish() {
        this.worldeditStatus = WorldeditStatus.FINISH;
        this.user.setWorldeditTask(null);
        this.worldeditManager.sendFinishMessage(user);

        this.giveItems(user.getPlayer(), this.needToGiveMaterials);
    }

    public Map<Material, Long> getMaterials() {
        return materials;
    }

    protected void add(Material material) {
        this.needToGiveMaterials.put(material, this.needToGiveMaterials.getOrDefault(material, 0L) + 1);
    }

    public void giveItems(Player player, Map<Material, Long> itemsToGive) {
        PlayerInventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        for (Map.Entry<Material, Long> entry : itemsToGive.entrySet()) {
            Material material = entry.getKey();
            long amount = entry.getValue();

            while (amount > 0) {
                int stackSize = (int) Math.min(amount, material.getMaxStackSize());
                ItemStack itemStack = new ItemStack(material, stackSize);

                System.out.println("> " + itemStack);
                Map<Integer, ItemStack> remainingItems = inventory.addItem(itemStack);
                System.out.println(">< " + remainingItems);

                if (!remainingItems.isEmpty()) {
                    for (ItemStack remainingItem : remainingItems.values()) {
                        System.out.println("Add: " + remainingItem);
                        vaultManager.addItem(player.getUniqueId(), remainingItem);
                    }
                }

                amount -= stackSize;
            }
        }
    }

    public void process() {

        var scheduler = this.plugin.getScheduler();

        int blocksPerSecond = worldeditManager.getBlocksPerSecond(user.getPlayer());

        int intervalTicks;
        int blocksPerTick;

        if (blocksPerSecond <= 20) {

            intervalTicks = 20 / blocksPerSecond;
            blocksPerTick = 1;
        } else {

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

            for (int i = 0; i < blocksPerTick; i++) {
                if (!blockInfos.isEmpty()) {
                    placeBlock(blockInfos.poll());
                }
            }

            if (blocksPerSecond > 20 && tickCounter.get() < additionalBlocks) {
                if (!blockInfos.isEmpty()) {
                    placeBlock(blockInfos.poll());
                }
            }

            tickCounter.getAndIncrement();

        }, 0L, intervalTicks);
    }

    private void placeBlock(BlockInfo blockInfo) {
        if (blockInfo == null) return;

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

    public int count() {
        return this.blockInfos.size();
    }
}
