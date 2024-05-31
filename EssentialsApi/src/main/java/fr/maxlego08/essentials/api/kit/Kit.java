package fr.maxlego08.essentials.api.kit;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.entity.Player;

import java.util.List;

public interface Kit {

    String getName();

    String getDisplayName();

    long getCooldown();

    List<MenuItemStack> getMenuItemStacks();

    void give(Player player);

    void setItems(List<MenuItemStack> menuItemStacks);

    List<Action> getActions();
}
