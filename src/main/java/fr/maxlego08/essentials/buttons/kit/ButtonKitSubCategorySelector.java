package fr.maxlego08.essentials.buttons.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.kit.KitModule;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ButtonKitSubCategorySelector extends Button {

    private final EssentialsPlugin plugin;
    private final String categoryName;
    private final String subCategoryName;

    public ButtonKitSubCategorySelector(EssentialsPlugin plugin, String categoryName, String subCategoryName) {
        this.plugin = plugin;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        
        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        List<Kit> kits = kitModule.getKitsBySubCategory(player, this.categoryName, this.subCategoryName);
        
        if (kits.isEmpty()) {
            return; // No kits in this sub-category
        }

        // Register placeholders for use in the menu
        placeholders.register("category", this.categoryName);
        placeholders.register("subcategory", this.subCategoryName);
        placeholders.register("kit_count", String.valueOf(kits.size()));
        
        // Note: The actual menu switching is handled by the menu configuration
        // This button just performs the click action
        
        super.onClick(player, event, inventory, slot, placeholders);
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        Placeholders placeholders = new Placeholders();
        
        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        List<Kit> kits = kitModule.getKitsBySubCategory(player, this.categoryName, this.subCategoryName);
        
        placeholders.register("category", this.categoryName);
        placeholders.register("subcategory", this.subCategoryName);
        placeholders.register("kit_count", String.valueOf(kits.size()));
        
        return this.getItemStack().build(player, false, placeholders);
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        KitModule kitModule = this.plugin.getModuleManager().getModule(KitModule.class);
        List<Kit> kits = kitModule.getKitsBySubCategory(player, this.categoryName, this.subCategoryName);
        
        // Only show sub-category if player has access to at least one kit in it
        return !kits.isEmpty() && super.checkPermission(player, inventory, placeholders);
    }
}
