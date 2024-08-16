package fr.maxlego08.essentials.api.worldedit;

import com.tcoded.folialib.wrapper.task.WrappedTask;
import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
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
    protected int blockAmounts = 0;
    protected WorldeditStatus worldeditStatus = WorldeditStatus.NOTHING;
    protected Map<Material, Long> materials = new HashMap<>();
    protected Map<Material, Long> needToGiveMaterials = new HashMap<>();
    protected List<Block> blocks = new ArrayList<>();
    protected BigDecimal totalPrice;
    protected WrappedTask wrappedTask;

    // Constructor initializes the WorldEditTask with required parameters.
    public WorldEditTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, Cuboid cuboid, List<MaterialPercent> materialPercents) {
        this.plugin = plugin;
        this.worldeditManager = worldeditManager;
        this.user = user;
        this.cuboid = cuboid;
        this.materialPercents = materialPercents;
    }

    // Abstract method to load the blocks for the world edit operation.
    public abstract void loadBlocks();

    // Abstract method to get the action type of the world edit operation (e.g., PLACE or REMOVE).
    public abstract WorldeditAction getAction();

    // Add rules to the list of Worldedit rules that will be applied during the task.
    protected void addRules(WorldeditRule... worldeditRules) {
        this.worldeditRules.addAll(Arrays.asList(worldeditRules));
    }

    // Calculates the total price of the world edit operation asynchronously and passes it to the given consumer.
    public void calculatePrice(Consumer<BigDecimal> consumer) {
        this.worldeditStatus = WorldeditStatus.CALCULATE_PRICE;

        this.plugin.getScheduler().runAsync(wrappedTask -> {

            // Load blocks to be processed
            this.loadBlocks();

            // Process the blocks in batches and calculate the total price
            processNextBatch(blocks.stream().map(b -> new BlockInfo(b, null, BigDecimal.ZERO)).collect(Collectors.toList()), 0, this.worldeditManager.getBatchSize(), () -> {

                this.totalPrice = this.blockInfos.stream().map(BlockInfo::price).reduce(BigDecimal.ZERO, BigDecimal::add);
                this.materials = this.blockInfos.stream().collect(Collectors.groupingBy(BlockInfo::newMaterial, Collectors.counting()));
                this.worldeditStatus = WorldeditStatus.WAITING_RESPONSE_PRICE;

                consumer.accept(totalPrice);
            }, blocks -> blocks.forEach(block -> this.processBlock(block.block())));
        });
    }

    // Processes the blocks in the given list in batches of the specified size.
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

    // Processes a single block according to the selected rules and materials.
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

    // Selects a random material based on the defined material percentages.
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

    // Returns the current status of the WorldEdit task.
    public WorldeditStatus getWorldeditStatus() {
        return worldeditStatus;
    }

    // Confirms whether the player has the required items for the operation.
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

    // Starts the process of placing blocks as per the WorldEdit operation.
    public void startPlaceBlocks() {

        this.worldeditStatus = WorldeditStatus.RUNNING;
        Player player = user.getPlayer();

        this.removeRequiredItems(player, this.materials);
        this.blockAmounts = this.blockInfos.size();

        this.process();

        if (!user.getOption(Option.WORLDEDIT_BOSSBAR_DISABLE)) {
            var bossBar = worldeditManager.getWorldeditBar();

            int speed = this.worldeditManager.getBlocksPerSecond(user.getPlayer());
            double seconds = (double) this.count() / (double) speed;
            bossBar.create(player, worldeditManager.getWorldeditConfiguration(), (long) (seconds * 1000.0));
        }
    }

    private void updateBossBar() {
        if (!user.getOption(Option.WORLDEDIT_BOSSBAR_DISABLE)) {

            var bossBar = worldeditManager.getWorldeditBar();
            int currentSize = this.count();
            int speed = this.worldeditManager.getBlocksPerSecond(user.getPlayer());
            double seconds = (double) this.count() / (double) speed;

            bossBar.update(user.getPlayer(), (float) currentSize / blockAmounts, (long) (seconds * 1000.0));
        }
    }

    // Returns the total price calculated for the WorldEdit operation.
    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    // Checks if the player has the required items in their inventory or vault.
    private boolean hasRequiredItems(Player player, Map<Material, Long> requiredItems) {
        Inventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        for (Map.Entry<Material, Long> entry : requiredItems.entrySet()) {
            Material material = entry.getKey();
            long requiredAmount = entry.getValue();

            long totalAmount = 0;
            if (useInventory()) {
                for (ItemStack item : inventory.getContents()) {
                    if (item != null && item.getType() == material) {
                        totalAmount += item.getAmount();
                    }
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

    // Removes the required items from the player's inventory and vault.
    public void removeRequiredItems(Player player, Map<Material, Long> requiredItems) {
        Inventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        // Remove the items from the inventory and vault if necessary
        for (Map.Entry<Material, Long> entry : requiredItems.entrySet()) {
            Material material = entry.getKey();
            long amountToRemove = entry.getValue();


            // Remove from inventory first
            if (useInventory()) {
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
            }

            // If items still need to be removed, remove from vault
            if (amountToRemove > 0) {
                vaultManager.removeMaterial(player, material, amountToRemove);
            }
        }
    }

    // Completes the WorldEdit task and sends a finish message to the user.
    protected void finish() {
        this.worldeditStatus = WorldeditStatus.FINISH;
        this.user.setWorldeditTask(null);
        this.worldeditManager.sendFinishMessage(user);

        this.giveItems(user.getPlayer(), this.needToGiveMaterials);

        if (!this.user.getOption(Option.WORLDEDIT_BOSSBAR_DISABLE)) {
            this.worldeditManager.getWorldeditBar().remove(user.getPlayer());
        }
    }

    // Returns the materials used in the WorldEdit operation.
    public Map<Material, Long> getMaterials() {
        return materials;
    }

    // Adds the specified material to the list of materials to be given back to the player.
    protected void add(Material material) {
        this.needToGiveMaterials.put(material, this.needToGiveMaterials.getOrDefault(material, 0L) + 1);
    }

    // Gives the specified items to the player, either directly to the inventory or to the vault if needed.
    public void giveItems(Player player, Map<Material, Long> itemsToGive) {

        var vaultManager = plugin.getVaultManager();

        if (useInventory()) {

            PlayerInventory inventory = player.getInventory();

            for (Map.Entry<Material, Long> entry : itemsToGive.entrySet()) {
                Material material = entry.getKey();
                long amount = entry.getValue();

                while (amount > 0) {
                    int stackSize = (int) Math.min(amount, material.getMaxStackSize());
                    ItemStack itemStack = new ItemStack(material, stackSize);
                    Map<Integer, ItemStack> remainingItems = inventory.addItem(itemStack);

                    if (!remainingItems.isEmpty()) {
                        for (ItemStack remainingItem : remainingItems.values()) {
                            vaultManager.addItem(player.getUniqueId(), remainingItem);
                        }
                    }

                    amount -= stackSize;
                }
            }
        } else {

            itemsToGive.forEach((material, amount) -> vaultManager.addItem(player.getUniqueId(), new ItemStack(material), amount));
        }
    }

    // Processes the block placing/removal operation according to the user's settings.
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

        this.wrappedTask = scheduler.runTimerAsync(() -> {

            if (blockInfos.isEmpty()) {
                this.wrappedTask.cancel();
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
            updateBossBar();

        }, 0L, intervalTicks);
    }

    // Places a block based on the BlockInfo data.
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

    // Returns the count of BlockInfo objects in the queue.
    public int count() {
        return this.blockInfos.size();
    }

    // Cancels the ongoing WorldEdit task and processes the refund for the user.
    public void cancel(Player player) {

        if (this.worldeditStatus != WorldeditStatus.RUNNING) return;

        this.worldeditStatus = WorldeditStatus.CANCELLED;
        this.wrappedTask.cancel();

        BigDecimal refundPrice = this.blockInfos.stream().map(BlockInfo::price).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<Material, Long> refundMaterials = getAction() == WorldeditAction.PLACE ? this.blockInfos.stream().collect(Collectors.groupingBy(BlockInfo::newMaterial, Collectors.counting())) : new HashMap<>();
        this.blockInfos.clear();

        var economy = this.plugin.getEconomyManager().getDefaultEconomy();
        this.user.deposit(economy, refundPrice);
        refundMaterials.putAll(this.needToGiveMaterials);

        giveItems(player, refundMaterials);

        this.worldeditManager.sendRefundMessage(player, refundMaterials, refundPrice, economy);

        this.user.setWorldeditTask(null);

        if (!this.user.getOption(Option.WORLDEDIT_BOSSBAR_DISABLE)) {
            this.worldeditManager.getWorldeditBar().remove(player);
        }
    }

    // Computes the squared length between three coordinates (x, y, z).
    protected double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    // Computes the squared length between two coordinates (x, z).
    protected double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    private boolean useInventory() {
        return this.user.getOption(Option.WORLDEDIT_INVENTORY);
    }
}
