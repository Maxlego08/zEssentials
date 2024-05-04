package fr.maxlego08.essentials.kit;

import fr.maxlego08.essentials.api.kit.Kit;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.entity.Player;

import java.util.List;

public class ZKit extends ZUtils implements Kit {

    private final String displayName;
    private final String name;
    private final long cooldown;
    private List<MenuItemStack> menuItemStacks;
    private final List<Action> actions;

    public ZKit(String displayName, String name, long cooldown, List<MenuItemStack> menuItemStacks, List<Action> actions) {
        this.displayName = displayName;
        this.name = name;
        this.cooldown = cooldown;
        this.menuItemStacks = menuItemStacks;
        this.actions = actions;
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
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public List<MenuItemStack> getMenuItemStacks() {
        return menuItemStacks;
    }

    @Override
    public void give(Player player) {
        this.menuItemStacks.forEach(menuItemStack -> give(player, menuItemStack.build(player, false)));
    }

    @Override
    public void setItems(List<MenuItemStack> menuItemStacks) {
        this.menuItemStacks = menuItemStacks;
    }

    @Override
    public List<Action> getActions() {
        return actions;
    }
}
