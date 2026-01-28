package fr.maxlego08.essentials.module.modules.kit;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.kit.KitDisplay;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Loader;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.common.utils.itemstack.MenuItemStackFromItemStack;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class KitModule extends ZModule {

    private final List<Kit> kits = new ArrayList<>();
    private final List<String> kitsOnFirstJoin = new ArrayList<>();
    private KitDisplay display;

    public KitModule(ZEssentialsPlugin plugin) {
        super(plugin, "kits");
        this.copyAndUpdate = false;
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadKits();

        this.loadInventory("kits");
        this.loadInventory("kit_preview");
        // this.loadInventory("kits_categories");
        // this.loadInventory("kits_category_combat");
        // this.loadInventory("kits_category_default");
    }

    /**
     * Checks if a kit exists by its name.
     *
     * @param name the name of the kit to check
     * @return true if the kit exists, false otherwise
     */
    public boolean exist(String name) {
        return this.getKit(name).isPresent();
    }

    /**
     * Retrieves a kit by its name.
     *
     * @param name the name of the kit to retrieve
     * @return an {@link Optional} containing the kit if found, or empty if not found
     */
    public Optional<Kit> getKit(String name) {
        return this.kits.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * Loads all kits from the "kits" directory. If the directory does not exist,
     * default kit files are saved from the resources. Clears the current list of kits
     * and repopulates it by loading each kit file found in the directory.
     */
    private void loadKits() {

        this.kits.clear();

        File folder = new File(getFolder(), "kits");
        if (!folder.exists()) {
            this.plugin.saveResource("modules/kits/kits/food.yml", true);
            this.plugin.saveResource("modules/kits/kits/tools.yml", true);
            this.plugin.saveResource("modules/kits/kits/fight.yml", true);
            this.plugin.saveResource("modules/kits/kits/basic_tools.yml", true);
            this.plugin.saveResource("modules/kits/kits/iron_tools_advanced.yml", true);
            this.plugin.saveResource("modules/kits/kits/pvp.yml", true);
            this.plugin.saveResource("modules/kits/kits/starter_food.yml", true);
        }

        files(folder, this::loadKit);
    }

    /**
     * Loads a kit from a file.
     *
     * @param file the file containing the kit configuration
     */
    private void loadKit(File file) {

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        String name = configuration.getString("name");
        String displayName = configuration.getString("display-name", name);
        String permission = configuration.getString("permission", Permission.ESSENTIALS_KIT_.asPermission(name));
        String category = configuration.getString("category", null);
        String subCategory = configuration.getString("sub-category", null);

        if (this.exist(name)) {
            this.plugin.getLogger().severe("Kit " + name + " already exist !");
            return;
        }

        long cooldown = configuration.getLong("cooldown", 0);
        Map<String, Long> permissionCooldowns = new HashMap<>();
        for (Map<?, ?> map : configuration.getMapList("permission-cooldowns")) {
            TypedMapAccessor accessor = new TypedMapAccessor((Map<String, Object>) map);
            permissionCooldowns.put(accessor.getString("permission"), accessor.getLong("cooldown"));
        }

        ConfigurationSection configurationSectionItems = configuration.getConfigurationSection("items");
        if (configurationSectionItems == null) {
            this.plugin.getLogger().severe("Impossible to find items section for " + file.getAbsoluteFile());
            return;
        }

        List<MenuItemStack> items = new ArrayList<>();

        for (String itemName : configurationSectionItems.getKeys(false)) {
            items.add(this.plugin.getInventoryManager().loadItemStack(configuration, "items." + itemName + ".", file));
        }

        List<Action> actions = this.plugin.getButtonManager().loadActions((List<Map<String, Object>>) configuration.getList("actions", new ArrayList<>()), "actions", file);

        var kit = new ZKit(this.plugin, displayName, name, category, subCategory, cooldown, permissionCooldowns, items, actions, permission, file);

        loadKitEquipment(kit, configuration, this.plugin.getInventoryManager(), "helmet.", EquipmentSlot.HEAD);
        loadKitEquipment(kit, configuration, this.plugin.getInventoryManager(), "chestplate.", EquipmentSlot.CHEST);
        loadKitEquipment(kit, configuration, this.plugin.getInventoryManager(), "leggings.", EquipmentSlot.LEGS);
        loadKitEquipment(kit, configuration, this.plugin.getInventoryManager(), "boots.", EquipmentSlot.FEET);

        this.kits.add(kit);
        this.plugin.getLogger().info("Register kit: " + name);
    }

    private void loadKitEquipment(ZKit kit, YamlConfiguration configuration, InventoryManager inventoryManager, String path, EquipmentSlot equipmentSlot) {
        if (configuration.getConfigurationSection(path) == null) return;
        var itemStack = inventoryManager.loadItemStack(configuration, path, kit.getFile());
        switch (equipmentSlot) {
            case HEAD -> kit.setHelmet(itemStack);
            case CHEST -> kit.setChestplate(itemStack);
            case LEGS -> kit.setLeggings(itemStack);
            case FEET -> kit.setBoots(itemStack);
        }
    }

    /**
     * Return a list of all kits that the given permissible has access to.
     *
     * @param permissible The permissible to check permissions for.
     * @return A list of kits that the permissible has access to.
     */
    public List<Kit> getKits(Permissible permissible) {
        return this.kits.stream().filter(kit -> kit.hasPermission(permissible)).toList();
    }

    /**
     * Gets all kits that belong to a specific category and the player has permission to use.
     *
     * @param player       The player to check permissions for
     * @param categoryName The name of the category
     * @return A list of kits in the specified category
     */
    public List<Kit> getKitsByCategory(Player player, String categoryName) {
        return getKits(player).stream().filter(e -> e.hasCategory() && e.getCategory().equalsIgnoreCase(categoryName)).toList();
    }

    /**
     * Gets all kits that belong to a specific subcategory within a category and the player has permission to use.
     *
     * @param player          The player to check permissions for
     * @param categoryName    The name of the category
     * @param subCategoryName The name of the subcategory
     * @return A list of kits in the specified subcategory
     */
    public List<Kit> getKitsBySubCategory(Player player, String categoryName, String subCategoryName) {
        return getKits(player).stream().filter(e -> e.getCategory().equalsIgnoreCase(categoryName) && e.getSubCategory().equalsIgnoreCase(subCategoryName)).toList();
    }

    public boolean giveKit(User user, Kit kit, boolean bypassCooldown) {

        long cooldown = kit.getCooldown(user.getPlayer());
        if (cooldown != 0 && !bypassCooldown && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN)) {
            if (user.isKitCooldown(kit)) {
                long milliSeconds = user.getKitCooldown(kit) - System.currentTimeMillis();
                message(user, Message.COOLDOWN, "%cooldown%", TimerBuilder.getStringTime(milliSeconds));
                return false;
            }
        }

        kit.give(user.getPlayer());

        if (cooldown != 0 && !bypassCooldown && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN)) {
            user.addKitCooldown(kit, cooldown);
        }

        return true;
    }

    /**
     * Sends a message to the specified command sender with a list of all available kits
     * formatted in a single line. Each kit name is clickable, allowing the user to execute
     * a command to obtain the kit.
     *
     * @param sender the command sender to send the message to
     */
    public void sendInLine(CommandSender sender) {
        List<String> homesAsString = kits.stream().map(kit -> getMessage(Message.COMMAND_KIT_INFORMATION_IN_LINE_INFO_AVAILABLE, "%name%", kit.getName())).toList();
        message(sender, Message.COMMAND_KIT_INFORMATION_IN_LINE, "%kits%", Strings.join(homesAsString, ','));
    }

    /**
     * Shows the kits to the given user, either in an inventory or as a message,
     * depending on the value of {@link #display}.
     * <p>
     * If {@link #display} is {@link KitDisplay#INVENTORY}, the kits are shown in an
     * inventory menu. Otherwise, the kits are shown as a message, with each kit
     * name clickable, allowing the user to execute a command to obtain the kit.
     * The message also shows whether the kit is available or not, and if not, the
     * time remaining until the kit is available again.
     *
     * @param user the user to show the kits to
     */
    public void showKits(User user) {

        if (display != KitDisplay.INVENTORY) {

            List<Kit> kits = getKits(user.getPlayer());

            if (display == KitDisplay.IN_LINE) {
                List<String> homesAsString = kits.stream().map(kit -> {

                    long cooldown = kit.getCooldown(user.getPlayer());
                    long milliSeconds = 0;
                    if (cooldown != 0 && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN) && user.isKitCooldown(kit)) {
                        milliSeconds = user.getKitCooldown(kit) - System.currentTimeMillis();
                    }

                    return getMessage(milliSeconds != 0 ? Message.COMMAND_KIT_INFORMATION_IN_LINE_INFO_UNAVAILABLE : Message.COMMAND_KIT_INFORMATION_IN_LINE_INFO_AVAILABLE, "%name%", kit.getName(), "%time%", TimerBuilder.getStringTime(milliSeconds));
                }).toList();
                message(user, Message.COMMAND_KIT_INFORMATION_IN_LINE, "%kits%", Strings.join(homesAsString, ','));
            } else {
                message(user, Message.COMMAND_KIT_INFORMATION_MULTI_LINE_HEADER);
                kits.forEach(kit -> {

                    long cooldown = kit.getCooldown(user.getPlayer());
                    long milliSeconds = 0;
                    if (cooldown != 0 && !user.hasPermission(Permission.ESSENTIALS_KIT_BYPASS_COOLDOWN) && user.isKitCooldown(kit)) {
                        milliSeconds = user.getKitCooldown(kit) - System.currentTimeMillis();
                    }

                    message(user, milliSeconds != 0 ? Message.COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_UNAVAILABLE : Message.COMMAND_KIT_INFORMATION_MULTI_LINE_CONTENT_AVAILABLE, "%name%", kit.getName(), "%time%", TimerBuilder.getStringTime(milliSeconds));
                });
                message(user, Message.COMMAND_KIT_INFORMATION_MULTI_LINE_FOOTER);
            }
        } else {

            this.plugin.openInventory(user.getPlayer(), "kits");
        }
    }

    /**
     * Opens the kit editor inventory for the specified player to edit the kit.
     *
     * @param player the player to open the kit editor for
     * @param kit    the kit to edit
     */
    public void openKitEditor(Player player, Kit kit) {
        InventoryHolder inventoryHolder = new KitInventoryHolder(player, kit);
        player.openInventory(inventoryHolder.getInventory());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof KitInventoryHolder inventoryHolder) {

            Kit kit = inventoryHolder.getKit();
            List<MenuItemStack> menuItemStacks = new ArrayList<>();
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack != null) {
                    menuItemStacks.add(MenuItemStackFromItemStack.fromItemStack(this.plugin.getInventoryManager(), itemStack));
                }
            }
            kit.setItems(menuItemStacks);

            saveKit(kit);
            message(event.getPlayer(), Message.COMMAND_KIT_EDITOR_SAVE, "%kit%", kit.getName());
        }
    }

    public void createKit(Player player, String kitName, long cooldown) {

        File file = new File(getFolder(), "kits/" + kitName + ".yml");
        try {
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        Kit kit = new ZKit(this.plugin, kitName, kitName, null, null, cooldown, new HashMap<>(), new ArrayList<>(), new ArrayList<>(), Permission.ESSENTIALS_KIT_.asPermission(kitName), file);

        this.kits.add(kit);
        this.saveKit(kit);

        message(player, Message.COMMAND_KIT_CREATE, "%kit%", kit.getName());
    }

    /**
     * Deletes the specified kit from the file system and removes it from the
     * list of available kits.
     *
     * @param player the player who is deleting the kit
     * @param kit    the kit to delete
     */
    public void deleteKit(Player player, Kit kit) {
        if (kit.getFile().delete()) {
            this.kits.remove(kit);
            message(player, Message.COMMAND_KIT_DELETE, "%kit%", kit.getName());
        } else {
            message(player, Message.COMMAND_KIT_DELETE, "%kit%", kit.getName());
        }
    }

    /**
     * Gets a list of default kit names that are not already in the list of kits.
     * <p>
     * This list of default kit names is used when creating a new kit, and the
     * names are filtered to exclude any that are already in use by existing kits.
     * <p>
     * The resulting list of kit names is a filtered list of the default names
     * that are not already in use by existing kits.
     *
     * @return a list of default kit names that are not already in use
     */
    public List<String> getKitNames() {
        List<String> kitNames = Arrays.asList("warrior", "archer", "mage", "healer", "miner", "builder", "scout", "assassin", "knight", "ranger", "alchemist", "blacksmith", "explorer", "thief", "fisherman", "farmer", "necromancer", "paladin", "berserker", "enchanter");
        return kitNames.stream().filter(name -> this.kits.stream().noneMatch(kit -> kit.getName().equalsIgnoreCase(name))).toList();
    }

    /**
     * Saves the specified kit to the file system.
     * <p>
     * This method saves the kit to its associated file, which is specified by the
     * {@link Kit#getFile()} method.
     *
     * @param kit the kit to save
     */
    private void saveKit(Kit kit) {
        var file = kit.getFile();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        // Clear items
        ConfigurationSection configurationSection = configuration.getConfigurationSection("items.");
        if (configurationSection != null) {
            configurationSection.getKeys(true).forEach(key -> configurationSection.set(key, null));
        }

        configuration.set("name", kit.getName());
        configuration.set("display-name", kit.getDisplayName());
        configuration.set("permission", kit.getPermission());
        configuration.set("cooldown", kit.getCooldown());

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
        AtomicInteger atomicInteger = new AtomicInteger(1);
        kit.getMenuItemStacks().forEach(menuItemStack -> loader.save(menuItemStack, configuration, "items.item-" + atomicInteger.getAndIncrement() + ".", file));

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        var user = getUser(event.getPlayer());
        if (user == null || this.kitsOnFirstJoin.isEmpty() || !user.isFirstJoin()) return;

        for (String kitName : this.kitsOnFirstJoin) {
            var optional = getKit(kitName);
            if (optional.isPresent()) {
                var kit = optional.get();
                kit.give(user.getPlayer());
            } else {
                this.plugin.getLogger().severe("Cannot find the kit " + kitName);
            }
        }
    }
}
