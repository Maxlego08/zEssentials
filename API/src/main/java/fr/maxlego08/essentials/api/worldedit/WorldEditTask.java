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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class WorldEditTask {

    protected final EssentialsPlugin plugin;
    protected final WorldeditManager worldeditManager;
    protected final User user;
    protected final List<Block> blocks;
    protected final List<MaterialPercent> materialPercents;
    protected final Queue<BlockInfo> blockInfos = new LinkedList<>();
    protected final Random random = new Random();
    protected WorldeditStatus worldeditStatus = WorldeditStatus.NOTHING;
    protected Map<Material, Long> materials = new HashMap<>();
    protected Map<Material, Long> needToGiveMaterials = new HashMap<>();
    protected BigDecimal totalPrice;

    public WorldEditTask(EssentialsPlugin plugin, WorldeditManager worldeditManager, User user, List<Block> blocks, List<MaterialPercent> materialPercents) {
        this.plugin = plugin;
        this.worldeditManager = worldeditManager;
        this.user = user;
        this.blocks = blocks;
        this.materialPercents = materialPercents;
    }

    public void calculatePrice(Consumer<BigDecimal> consumer) {
        this.worldeditStatus = WorldeditStatus.CALCULATE_PRICE;
        processNextBatch(blocks.stream().map(b -> new BlockInfo(b, null, BigDecimal.ZERO)).collect(Collectors.toList()), 0, 100, () -> {

            this.totalPrice = this.blockInfos.stream().map(BlockInfo::price).reduce(BigDecimal.ZERO, BigDecimal::add);
            this.materials = this.blockInfos.stream().collect(Collectors.groupingBy(BlockInfo::newMaterial, Collectors.counting()));
            this.worldeditStatus = WorldeditStatus.WAITING_RESPONSE_PRICE;

            consumer.accept(totalPrice);
        }, blocks -> blocks.forEach(block -> this.processBlock(block.block())));
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

        BigDecimal blockPrice = this.worldeditManager.getMaterialPrice(randomMaterial);

        System.out.println(blockPrice + " - " + randomMaterial);

        this.blockInfos.add(new BlockInfo(block, randomMaterial, blockPrice));
    }

    public abstract void process();

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
            var result = hasRequiredItems(player, materials);

            if (!result) this.worldeditStatus = WorldeditStatus.NOT_ENOUGH_ITEMS;

            consumer.accept(result);
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

    public boolean removeRequiredItems(Player player, Map<Material, Long> requiredItems) {
        Inventory inventory = player.getInventory();
        var vaultManager = plugin.getVaultManager();

        // Vérifier d'abord si le joueur a bien tous les items nécessaires
        if (!hasRequiredItems(player, requiredItems)) {
            return false;
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

        return true;
    }


    protected void finish() {
        this.worldeditStatus = WorldeditStatus.FINISH;
        this.user.setWorldeditTask(null);

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

            // Créer les ItemStacks et les ajouter à l'inventaire du joueur
            while (amount > 0) {
                int stackSize = (int) Math.min(amount, material.getMaxStackSize());
                ItemStack itemStack = new ItemStack(material, stackSize);

                // Essayer d'ajouter l'item à l'inventaire du joueur
                Map<Integer, ItemStack> remainingItems = inventory.addItem(itemStack);

                // Si l'item n'a pas pu être ajouté entièrement (inventaire plein)
                if (!remainingItems.isEmpty()) {
                    for (ItemStack remainingItem : remainingItems.values()) {
                        vaultManager.addItem(player.getUniqueId(), remainingItem);
                    }
                    break;
                }

                amount -= stackSize;
            }
        }
    }
}
