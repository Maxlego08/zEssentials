package fr.maxlego08.essentials.kit;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.kit.KitDisplay;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class KitModule extends ZModule {

    private final List<Kit> kits = new ArrayList<>();
    private final KitDisplay display = KitDisplay.IN_LINE;

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
    }

    public boolean exist(String name) {
        return this.getKit(name).isPresent();
    }

    public Optional<Kit> getKit(String name) {
        return this.kits.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
    }

    private void loadKits() {

        this.kits.clear();

        YamlConfiguration configuration = getConfiguration();
        File file = new File(getFolder(), "config.yml");

        ConfigurationSection configurationSection = configuration.getConfigurationSection("kits");
        if (configurationSection == null) return;

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());

        for (String key : configurationSection.getKeys(false)) {

            String path = "kits." + key + ".";
            String name = configuration.getString(path + "name");
            long cooldown = configuration.getLong(path + "cooldown");

            ConfigurationSection configurationSectionItems = configuration.getConfigurationSection(path + "items");
            if (configurationSectionItems == null) continue;

            List<MenuItemStack> menuItemStacks = new ArrayList<>();

            for (String itemName : configurationSectionItems.getKeys(false)) {
                try {
                    menuItemStacks.add(loader.load(configuration, path + "items." + itemName + ".", file));
                } catch (InventoryException exception) {
                    exception.printStackTrace();
                }
            }

            if (this.exist(name)) {
                this.plugin.getLogger().severe("Kit " + name + " already exist !");
                return;
            }

            List<Action> actions = this.plugin.getButtonManager().loadActions((List<Map<String, Object>>) configuration.getList(path + "actions", new ArrayList<>()), path, file);

            Kit kit = new ZKit(name, key, cooldown, menuItemStacks, actions);
            this.kits.add(kit);
            this.plugin.getLogger().info("Register kit: " + name);
        }
    }

    public void saveKits() {

        YamlConfiguration configuration = getConfiguration();
        File file = new File(getFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        this.kits.forEach(kit -> {

            String path = "kits." + kit.getName() + ".";
            ConfigurationSection configurationSection = configuration.getConfigurationSection(path + "items");
            if (configurationSection != null) {
                configurationSection.getKeys(true).forEach(key -> configurationSection.set(key, null));
            }
            configuration.set(path + "name", kit.getDisplayName());

            if (kit.getCooldown() > 0) configuration.set(path + "cooldown", kit.getCooldown());
            else configuration.set(path + "cooldown", null);

            Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
            AtomicInteger atomicInteger = new AtomicInteger(1);
            kit.getMenuItemStacks().forEach(menuItemStack -> loader.save(menuItemStack, configuration, path + "items.item" + atomicInteger.getAndIncrement() + ".", file));

        });

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public List<Kit> getKits(Permissible permissible) {
        return this.kits.stream().filter(kit -> permissible.hasPermission(Permission.ESSENTIALS_KIT.asPermission(kit.getName()))).toList();
    }

    public boolean giveKit(User user, Kit kit, boolean bypassCooldown) {

        long cooldown = kit.getCooldown();
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

        kit.getActions().forEach(action -> action.preExecute(user.getPlayer(), null, this.plugin.getInventoryManager().getFakeInventory(), new Placeholders()));

        return true;
    }

    public void showKits(User user) {

        if (display != KitDisplay.INVENTORY) {

            List<Kit> kits = getKits(user.getPlayer());

            if (display == KitDisplay.IN_LINE) {
                List<String> homesAsString = kits.stream().map(kit -> {

                    long cooldown = kit.getCooldown();
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

                    long cooldown = kit.getCooldown();
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
                    menuItemStacks.add(MenuItemStack.fromItemStack(this.plugin.getInventoryManager(), itemStack));
                }
            }
            kit.setItems(menuItemStacks);
            this.saveKits();
            message(event.getPlayer(), Message.COMMAND_KIT_EDITOR_SAVE, "%kit%", kit.getName());
        }
    }

    public void createKit(Player player, String kitName, int cooldown) {

        Kit kit = new ZKit(kitName, kitName, cooldown, new ArrayList<>(), new ArrayList<>());
        kits.add(kit);
        this.saveKits();

        message(player, Message.COMMAND_KIT_CREATE, "%kit%", kit.getName());
    }

    public void deleteKit(Player player, Kit kit) {
        kits.remove(kit);
        this.saveKits();

        message(player, Message.COMMAND_KIT_DELETE, "%kit%", kit.getName());
    }
}
