package fr.maxlego08.essentials.worldedit;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.configuration.NonLoadable;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.api.worldedit.BlockPrice;
import fr.maxlego08.essentials.api.worldedit.MaterialPercent;
import fr.maxlego08.essentials.api.worldedit.PermissionBlockPerSecond;
import fr.maxlego08.essentials.api.worldedit.PermissionMaxBlocks;
import fr.maxlego08.essentials.api.worldedit.PermissionMaxDistance;
import fr.maxlego08.essentials.api.worldedit.Selection;
import fr.maxlego08.essentials.api.worldedit.WorldEditItem;
import fr.maxlego08.essentials.api.worldedit.WorldEditTask;
import fr.maxlego08.essentials.api.worldedit.WorldeditManager;
import fr.maxlego08.essentials.api.worldedit.WorldeditStatus;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.worldedit.taks.CutTask;
import fr.maxlego08.essentials.worldedit.taks.SetTask;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldeditModule extends ZModule implements WorldeditManager {

    @NonLoadable
    private final List<WorldEditItem> worldEditItems = new ArrayList<>();
    private final List<String> blacklistBlocks = new ArrayList<>();
    private BigDecimal defaultBlockPrice;
    private List<BlockPrice> blocksPrice;
    private List<PermissionBlockPerSecond> permissionsBlocksPerSecond;
    private List<PermissionMaxBlocks> permissionsMaxBlocks;
    private List<PermissionMaxDistance> permissionsMaxDistances;
    private int batchSize;

    public WorldeditModule(ZEssentialsPlugin plugin) {
        super(plugin, "worldedit");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        YamlConfiguration configuration = getConfiguration();
        var section = configuration.getConfigurationSection("items");
        if (section == null) return;

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());

        this.worldEditItems.clear();
        for (String key : section.getKeys(false)) {

            try {
                String path = "items." + key + ".";
                int maxUse = configuration.getInt(path + "max-use", -1);
                int maxBlocks = configuration.getInt(path + "max-blocks", -1);
                int maxDistance = configuration.getInt(path + "max-distance", -1);
                double priceMultiplier = configuration.getDouble(path + "price-multiplier", -1);
                MenuItemStack menuItemStack = loader.load(configuration, path + "item.", new File(getFolder(), "config.yml"));

                WorldEditItem worldEditItem = new WorldEditItem(key, maxUse, priceMultiplier, maxBlocks, maxDistance, menuItemStack);
                this.worldEditItems.add(worldEditItem);

            } catch (InventoryException exception) {
                exception.printStackTrace();
            }
        }

        System.out.println(permissionsBlocksPerSecond);
        System.out.println(permissionsMaxBlocks);
        System.out.println(permissionsMaxDistances);
    }

    @Override
    public Optional<WorldEditItem> getWorldeditItem(String name) {
        return this.worldEditItems.stream().filter(worldEditItem -> worldEditItem.name().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void give(CommandSender sender, Player player, String itemName) {

        var optional = getWorldeditItem(itemName);
        if (optional.isEmpty()) {
            message(sender, Message.COMMAND_WORLDEDIT_GIVE_ERROR, "%name%", itemName);
            return;
        }

        var worldeditItem = optional.get();
        ItemStack itemStack = worldeditItem.getItemStack(player, 0);
        plugin.give(player, itemStack);

        message(sender, Message.COMMAND_WORLDEDIT_GIVE_SENDER, "%player%", player.getName(), "%item%", itemName);
        message(player, Message.COMMAND_WORLDEDIT_GIVE_RECEIVER, "%item%", itemName);
    }

    @Override
    public List<String> getWorldeditItems() {
        return this.worldEditItems.stream().map(WorldEditItem::name).toList();
    }

    @Override
    public List<String> getAllowedMaterials(Player player) {

        Set<Material> blockMaterials = Stream.of(player.getInventory().getContents()).filter(item -> item != null && item.getType().isBlock()).map(ItemStack::getType).collect(Collectors.toSet());
        blockMaterials.addAll(this.plugin.getVaultManager().getMaterials(player));

        return blockMaterials.stream().filter(material -> material.isBlock() && !isBlacklist(material)).map(material -> material.name().toLowerCase()).toList();
    }

    @Override
    public boolean isBlacklist(Material material) {
        return this.blacklistBlocks.contains(material.name());
    }

    private boolean cantUseWorldEdit(User user) {
        var selection = user.getSelection();
        if (!selection.isValid()) {
            message(user, Message.WORLDEDIT_SELECTION_ERROR);
            return true;
        }

        int maxBlocks = getMaxBlocks(user.getPlayer());
        if (selection.getCuboid().getVolume() > maxBlocks) {
            message(user, Message.WORLDEDIT_SELECTION_VOLUME, "%blocks%", maxBlocks);
            return true;
        }

        int maxDistance = getMaxDistance(user.getPlayer());
        if (selection.getCuboid().getDistance() > maxDistance) {
            message(user, Message.WORLDEDIT_SELECTION_DISTANCE, "%distance%", maxDistance);
            return true;
        }

        if (user.hasWorldeditTask()) {
            message(user, Message.WORLDEDIT_ALREADY_RUNNING);
            return true;
        }

        int speed = getBlocksPerSecond(user.getPlayer());
        if (speed <= 0) {
            message(user, Message.WORLDEDIT_SPEED_ERROR);
            return true;
        }
        return false;
    }

    @Override
    public void setBlocks(User user, List<MaterialPercent> materialPercents) {
        var selection = user.getSelection();
        if (cantUseWorldEdit(user)) return;

        WorldEditTask worldEditTask = new SetTask(this.plugin, this, user, selection.getCuboid(), materialPercents);
        placeBlock(user, worldEditTask);
    }

    @Override
    public void fillBlocks(User user, List<MaterialPercent> materialPercents) {
        var selection = user.getSelection();
        if (cantUseWorldEdit(user)) return;

        WorldEditTask worldEditTask = new SetTask(this.plugin, this, user, selection.getCuboid(), materialPercents);
        placeBlock(user, worldEditTask);
    }

    private void placeBlock(User user, WorldEditTask worldEditTask) {

        int speed = getBlocksPerSecond(user.getPlayer());
        user.setWorldeditTask(worldEditTask);

        message(user, Message.WORLDEDIT_START_CALCULATE_PRICE);

        worldEditTask.calculatePrice(price -> {

            var economyManager = this.plugin.getEconomyManager();
            var economy = economyManager.getDefaultEconomy();

            if (!user.has(economy, price)) {
                message(user, Message.WORLDEDIT_NOT_ENOUGH_MONEY);
                user.setWorldeditTask(null);
                return;
            }

            int blocks = worldEditTask.count();
            double seconds = (double) blocks / (double) speed;

            String materials = worldEditTask.getMaterials().entrySet().stream().map(entry -> {

                var material = entry.getKey();
                var amount = entry.getValue();
                var blockPrice = getMaterialPrice(material);

                return getMessage(Message.COMMAND_WORLDEDIT_CONFIRM_MATERIAL, "%translation-key%", material.translationKey(), "%amount%", amount, "%price%", economyManager.format(economy, blockPrice.multiply(BigDecimal.valueOf(amount))), "%price-per-block%", economyManager.format(economy, blockPrice));
            }).collect(Collectors.joining(","));

            message(user, Message.COMMAND_WORLDEDIT_CONFIRM_PRICE, "%price%", economyManager.format(economy, price), "%materials%", materials, "%duration%", TimerBuilder.getStringTime(seconds * 1000), "%blocks%", blocks, "%speed%", speed, "%s%", speed > 1 ? "s" : "");
        });
    }

    @Override
    public void cutBlocks(User user) {

        var selection = user.getSelection();
        int speed = getBlocksPerSecond(user.getPlayer());
        if (cantUseWorldEdit(user)) return;

        WorldEditTask worldEditTask = new CutTask(this.plugin, this, user, selection.getCuboid());
        user.setWorldeditTask(worldEditTask);

        message(user, Message.WORLDEDIT_START_CALCULATE_PRICE);

        worldEditTask.calculatePrice(price -> {

            var economyManager = this.plugin.getEconomyManager();
            var economy = economyManager.getDefaultEconomy();

            if (!user.has(economy, price)) {
                message(user, Message.WORLDEDIT_NOT_ENOUGH_MONEY);
                user.setWorldeditTask(null);
                return;
            }

            int blocks = worldEditTask.count();
            double seconds = (double) blocks / (double) speed;

            message(user, Message.COMMAND_WORLDEDIT_CONFIRM_PRICE_CUT, "%price%", economyManager.format(economy, price), "%duration%", TimerBuilder.getStringTime(seconds * 1000), "%blocks%", blocks, "%speed%", speed, "%s%", speed > 1 ? "s" : "");
        });

    }

    @Override
    public void confirmAction(User user) {
        if (user.hasWorldeditTask()) {
            message(user, Message.WORLDEDIT_ALREADY_RUNNING);
            return;
        }

        var task = user.getWorldeditTask();
        if (task.getWorldeditStatus() != WorldeditStatus.WAITING_RESPONSE_PRICE) {
            message(user, Message.COMMAND_WORLDEDIT_CONFIRM_ERROR);
            return;
        }

        message(user, Message.WORLDEDIT_START_CHECK_INVENTORY);

        var economy = plugin.getEconomyManager().getDefaultEconomy();

        task.confirm(result -> {

            if (result) {

                if (!user.has(economy, task.getTotalPrice())) {
                    message(user, Message.WORLDEDIT_NOT_ENOUGH_MONEY);
                    user.setWorldeditTask(null);
                    return;
                }

                user.withdraw(economy, task.getTotalPrice());
                message(user, Message.WORLDEDIT_START_RUNNING);

                task.startPlaceBlocks();

            } else {

                message(user, Message.WORLDEDIT_NOT_ENOUGH_ITEMS);
            }
        });
    }

    private boolean isWorldeditItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.has(WorldEditItem.KEY_WORLDEDIT, PersistentDataType.STRING);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {

        var block = event.getClickedBlock();
        var item = event.getItem();

        if (item == null || block == null || event.useInteractedBlock() == Event.Result.DENY || event.getHand() == EquipmentSlot.OFF_HAND || event.getAction().equals(Action.PHYSICAL) || !isWorldeditItem(item)) {
            return;
        }

        User user = this.getUser(event.getPlayer());
        if (user == null) return;

        Selection selection = user.getSelection();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            event.setCancelled(true);
            selection.setFirstLocation(block.getLocation());
            message(event.getPlayer(), Message.WORLDEDIT_SELECTION_POS1);

        } else {

            event.setCancelled(true);
            selection.setSecondLocation(block.getLocation());
            message(event.getPlayer(), Message.WORLDEDIT_SELECTION_POS2);
        }
    }

    @Override
    public BigDecimal getMaterialPrice(Material material) {
        return this.blocksPrice.stream().filter(blockPrice -> blockPrice.material() == material).map(BlockPrice::price).findFirst().orElse(this.defaultBlockPrice);
    }

    @Override
    public int getBlocksPerSecond(Player player) {
        return this.permissionsBlocksPerSecond.stream().filter(permissionBlockPerSecond -> player.hasPermission(permissionBlockPerSecond.permission())).mapToInt(PermissionBlockPerSecond::blocks).max().orElse(0);
    }

    @Override
    public int getMaxBlocks(Player player) {
        return this.permissionsMaxBlocks.stream().filter(permissionMaxBlocks -> player.hasPermission(permissionMaxBlocks.permission())).mapToInt(PermissionMaxBlocks::blocks).max().orElse(0);
    }

    @Override
    public int getMaxDistance(Player player) {
        return this.permissionsMaxDistances.stream().filter(permissionMaxDistance -> player.hasPermission(permissionMaxDistance.permission())).mapToInt(PermissionMaxDistance::distance).max().orElse(0);
    }

    @Override
    public void sendFinishMessage(User user) {
        message(user, Message.WORLDEDIT_FINISH);
    }

    @Override
    public int getBatchSize() {
        return this.batchSize;
    }
}
