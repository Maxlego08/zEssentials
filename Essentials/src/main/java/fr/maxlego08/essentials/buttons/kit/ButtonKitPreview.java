package fr.maxlego08.essentials.buttons.kit;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ButtonKitPreview extends ZButton {

    private final EssentialsPlugin plugin;

    public ButtonKitPreview(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        User user = this.plugin.getUser(player.getUniqueId());
        if (user == null) return;

        Kit kit = user.getKitPreview();
        if (kit == null) return;

        for (int index = 0; index != Math.min(this.slots.size(), kit.getMenuItemStacks().size()); index++) {
            int slot = this.slots.get(index);
            MenuItemStack menuItemStack = kit.getMenuItemStacks().get(index);
            inventory.addItem(slot, menuItemStack.build(player, false));
        }
    }
}
