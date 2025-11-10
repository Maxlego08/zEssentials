package fr.maxlego08.essentials.api.kit;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.requirement.Action;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.io.File;
import java.util.List;
import java.util.Map;

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
     * Gets the cooldown time for the kit in milliseconds, taking into
     * account any permissions the specified permissible entity may have.
     *
     * @param permissible the permissible entity to check permissions for
     * @return the cooldown time in milliseconds, or 0 if no cooldown is
     * specified
     */
    long getCooldown(Permissible permissible);

    /**
     * Gets the cooldown time for the kit in milliseconds.
     *
     * @return the cooldown time in milliseconds
     */
    long getCooldown();

    Map<String, Long> getPermissionCooldowns();

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
     * @param permissible the command sender to check permissions for
     * @return true if the sender has permission, false otherwise
     */
    boolean hasPermission(Permissible permissible);

    /**
     * Gets the permission string required to use this kit.
     *
     * @return the permission string
     */
    String getPermission();

    /**
     * Gets the file associated with this kit.
     *
     * @return the file associated with this kit
     */
    File getFile();

    /**
     * Gets the menu item stack associated with the player's helmet.
     *
     * @return the menu item stack associated with the player's helmet
     */
    MenuItemStack getHelmet();

    /**
     * Gets the menu item stack associated with the player's chestplate.
     *
     * @return the menu item stack associated with the player's chestplate
     */
    MenuItemStack getChestplate();

    /**
     * Gets the menu item stack associated with the player's leggings.
     *
     * @return the menu item stack associated with the player's leggings
     */
    MenuItemStack getLeggings();

    /**
     * Gets the menu item stack associated with the player's boots.
     *
     * @return the menu item stack associated with the player's boots
     */
    MenuItemStack getBoots();
}

