package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.HomeModule;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonHomes extends ZButton implements PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonHomes(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        List<Home> homes = user.getHomes();
        Pagination<Home> pagination = new Pagination<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        pagination.paginate(homes, this.slots.size(), inventory.getPage()).forEach(home -> displayHome(this.slots.get(atomicInteger.getAndIncrement()), home, player, user, inventory));
    }

    private void displayHome(int slot, Home home, Player player, User user, InventoryDefault inventory) {

        MenuItemStack menuItemStack = this.getItemStack();
        HomeModule homeModule = this.plugin.getModuleManager().getModule(HomeModule.class);
        Placeholders placeholders = homeModule.getHomePlaceholders(home, user.countHomes(), homeModule.getMaxHome(player));
        Material material = home.getMaterial();
        placeholders.register("material", material == null ? Material.BLUE_BED.name() : material.name());

        inventory.addItem(slot, menuItemStack.build(player, false, placeholders)).setClick(event -> {
            if (event.isRightClick()) {

                homeModule.deleteHome(player, user, home.getName());
            } else if (event.isLeftClick()) {

                player.closeInventory();
                homeModule.teleport(user, home);
            } else if (event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.DROP) {

                homeModule.changeDisplayItem(player, home);
            }
        });
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        User user = plugin.getUser(player.getUniqueId());
        return user != null && user.countHomes() > 0;
    }

    @Override
    public int getPaginationSize(Player player) {
        User user = plugin.getUser(player.getUniqueId());
        return user == null ? 0 : user.countHomes();
    }
}
