package fr.maxlego08.essentials.kit;

import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.MenuItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public class ZKit extends ZUtils implements Kit {

    private final String name;
    private final long cooldown;
    private final List<MenuItemStack> menuItemStacks;

    public ZKit(String name, long cooldown, List<MenuItemStack> menuItemStacks) {
        this.name = name;
        this.cooldown = cooldown;
        this.menuItemStacks = menuItemStacks;
    }

    @Override
    public long getCooldown() {
        return this.cooldown;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<MenuItemStack> getMenuItemStacks() {
        return menuItemStacks;
    }

    @Override
    public void give(Player player) {
        this.menuItemStacks.forEach(menuItemStack -> give(player, menuItemStack.build(player, false)));
    }
}
