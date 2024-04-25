package fr.maxlego08.essentials.api.kit;

import fr.maxlego08.menu.MenuItemStack;
import org.bukkit.entity.Player;

import java.util.List;

public interface Kit {

    String getName();

    long getCooldown();

    List<MenuItemStack> getMenuItemStacks();

    void give(Player player);
}
