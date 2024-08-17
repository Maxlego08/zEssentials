package fr.maxlego08.essentials.api.kit;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Represents a kit in the game, which is a predefined set of items or actions
 * that can be given to players.
 */
public interface Kit {

    /**
     * Gets the name of the kit.
     *
     * @return the name of the kit
     */
    String getName();

    /**
     * Gets the display name of the kit, which may include formatting or colors.
     *
     * @return the display name of the kit
     */
    String getDisplayName();

    /**
     * Gets the cooldown time for the kit in milliseconds.
     *
     * @return the cooldown time in milliseconds
     */
    long getCooldown();

    /**
     * Gets the list of menu item stacks associated with this kit.
     *
     * @return a list of {@link MenuItemStack} objects
     */
    List<MenuItemStack> getMenuItemStacks();

    /**
     * Gives the kit to the specified player.
     *
     * @param player the player to give the kit to
     */
    void give(Player player);

    /**
     * Sets the items in the kit using a list of menu item stacks.
     *
     * @param menuItemStacks the list of {@link MenuItemStack} objects to set
     */
    void setItems(List<MenuItemStack> menuItemStacks);

    /**
     * Gets the list of actions associated with this kit.
     *
     * @return a list of {@link Action} objects
     */
    List<Action> getActions();

    /**
     * Checks if the sender has permission to use this kit.
     *
     * @param sender the command sender to check permissions for
     * @return true if the sender has permission, false otherwise
     */
    boolean hasPermission(CommandSender sender);
}

