package fr.maxlego08.essentials.buttons.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.Pagination;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonKitCategory extends PaginateButton {

    private final EssentialsPlugin plugin;
    private final String categoryName;

    public ButtonKitCategory(Plugin plugin, String categoryName) {
        this.plugin = (EssentialsPlugin) plugin;
        this.categoryName = categoryName;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {
        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        List<Kit> kits = kitModule.getKitsByCategory(player, this.categoryName);
        
        Pagination<Kit> pagination = new Pagination<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        pagination.paginate(kits, this.slots.size(), inventory.getPage()).forEach(kit -> 
            displayKit(this.slots.get(atomicInteger.getAndIncrement()), kit, player, user, inventory)
        );
    }

    private void displayKit(int slot, Kit kit, Player player, User user, InventoryEngine inventory) {
        MenuItemStack menuItemStack = this.getItemStack();
        Placeholders placeholders = new Placeholders();
        
        placeholders.register("kit_name", kit.getName());
        placeholders.register("kit_display_name", kit.getDisplayName());
        placeholders.register("category", this.categoryName);
        placeholders.register("cooldown", TimerBuilder.getStringTime(kit.getCooldown() * 1000));
        
        // Check if player is on cooldown
        long cooldown = kit.getCooldown(player);
        if (cooldown != 0 && user.isKitCooldown(kit)) {
            long milliSeconds = user.getKitCooldown(kit) - System.currentTimeMillis();
            placeholders.register("time_remaining", TimerBuilder.getStringTime(milliSeconds));
            placeholders.register("available", "false");
        } else {
            placeholders.register("time_remaining", "0");
            placeholders.register("available", "true");
        }
        
        inventory.addItem(slot, menuItemStack.build(player, false, placeholders)).setClick(event -> {
            if (event.isLeftClick()) {
                this.plugin.giveKit(user, kit, false);
            } else if (event.isRightClick()) {
                user.openKitPreview(kit);
            }
        });
    }

    @Override
    public int getPaginationSize(Player player) {
        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        return kitModule.getKitsByCategory(player, this.categoryName).size();
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        return !kitModule.getKitsByCategory(player, this.categoryName).isEmpty() && 
               super.checkPermission(player, inventory, placeholders);
    }
}
