package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.home.Home;
import fr.maxlego08.essentials.api.home.HomeManager;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.Pagination;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonHomes extends PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonHomes(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {

        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;

        List<Home> homes = user.getHomes();
        Pagination<Home> pagination = new Pagination<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        pagination.paginate(homes, this.slots.size(), inventory.getPage()).forEach(home -> displayHome(this.slots.get(atomicInteger.getAndIncrement()), home, player, user, inventory));
    }

    private void displayHome(int slot, Home home, Player player, User user, InventoryEngine inventory) {

        MenuItemStack menuItemStack = this.getItemStack();
        HomeManager homeManager = plugin.getHomeManager();
        Placeholders placeholders = homeManager.getHomePlaceholders(home, user.countHomes(), homeManager.getMaxHome(player));
        Material material = home.getMaterial();
        placeholders.register("material", material == null ? homeManager.getDefaultHomeMaterial() : material.name());

        inventory.addItem(slot, menuItemStack.build(player, false, placeholders)).setClick(event -> {
            if (event.isRightClick()) {

                if (homeManager.isHomeDeleteConfirm()) {
                    homeManager.openInventoryConfirmHome(user, home);
                } else {
                    homeManager.deleteHome(player, user, home.getName());
                }
            } else if (event.isLeftClick()) {

                player.closeInventory();
                homeManager.teleport(user, home);
            } else if (event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.DROP) {

                homeManager.changeDisplayItem(player, home);
            }
        });
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        User user = plugin.getUser(player.getUniqueId());
        return user != null && user.countHomes() > 0;
    }

    @Override
    public int getPaginationSize(Player player) {
        User user = plugin.getUser(player.getUniqueId());
        return user == null ? 0 : user.countHomes();
    }
}
