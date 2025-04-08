package fr.maxlego08.essentials.api.home;

import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import java.util.List;

public interface HomeManager {

    /**
     * Get the list of {@link HomePermission}.
     *
     * @return the list of home permissions.
     */
    List<HomePermission> getPermissions();

    /**
     * Checks if the given name is valid for a home.
     *
     * @param input the home name to check.
     * @return a message explaining why the home name is not valid, or null if it is valid.
     */
    Message isValidHomeName(String input);

    /**
     * Gets the maximum number of homes allowed for the given permissible entity.
     *
     * @param permissible the permissible entity.
     * @return the maximum number of homes allowed.
     */
    int getMaxHome(Permissible permissible);

    /**
     * Sends the list of homes to the given player.
     *
     * @param player the player to send the list of homes to.
     * @param user the user owning the homes.
     */
    void sendHomes(Player player, User user);

    /**
     * Opens the home inventory for the given player.
     * <p>
     * This can be a list of homes in an inventory, or a message with a list of homes.
     *
     * @param player the player to open the home inventory for.
     */
    void openInventory(Player player);

    /**
     * Opens the confirmation inventory to delete a home.
     *
     * @param user the user owning the home.
     * @param home the home to delete.
     */
    void openInventoryConfirmHome(User user, Home home);

    /**
     * Gets the placeholders for the given home.
     * <p>
     * The placeholders are used to format messages with home information.
     *
     * @param home the home.
     * @param homeAmount the number of homes the user has.
     * @param maxHome the maximum number of homes the user can have.
     * @return the placeholders for the given home.
     */
    Placeholders getHomePlaceholders(Home home, int homeAmount, int maxHome);

    /**
     * Teleports the given user to the given home.
     * <p>
     * If the home does not exist, this method does nothing.
     *
     * @param user the user to teleport.
     * @param home the home to teleport to.
     */
    void teleport(User user, Home home);

    /**
     * Changes the display item for the given home.
     * <p>
     * If the player is holding an item, the display item of the home will be
     * changed to the item the player is holding. If the player is not holding an
     * item, the display item of the home will be reset to the default material.
     *
     * @param player the player to change the display item for.
     * @param home the home to change the display item for.
     */
    void changeDisplayItem(Player player, Home home);

    /**
     * Deletes the home with the given name from the given user.
     * <p>
     * If the home does not exist, this method does nothing.
     *
     * @param player the player to delete the home for.
     * @param user the user to delete the home from.
     * @param homeName the name of the home to delete.
     */
    void deleteHome(Player player, User user, String homeName);

    /**
     * Teleports the given user to the home with the given name of the given player.
     * <p>
     * If the home does not exist, this method does nothing.
     *
     * @param user the user to teleport.
     * @param username the username of the player owning the home.
     * @param homeName the name of the home to teleport to.
     */
    void teleport(User user, String username, String homeName);

    /**
     * Deletes the home with the given name from the given player.
     * <p>
     * If the home does not exist, this method does nothing.
     *
     * @param sender the command sender to delete the home for.
     * @param username the username of the player owning the home.
     * @param homeName the name of the home to delete.
     */
    void deleteHome(CommandSender sender, String username, String homeName);

    /**
     * Sets the home with the given name for the given user.
     * <p>
     * If the home already exists, this method will overwrite the existing home.
     * If the home does not exist, this method will create a new home.
     * <p>
     * The home will be set to the location of the given player.
     *
     * @param player the player to set the home for.
     * @param username the username of the player owning the home.
     * @param homeName the name of the home to set.
     */
    void setHome(Player player, String username, String homeName);

    /**
     * Gets the list of worlds in which homes are disabled.
     *
     * @return the list of worlds in which homes are disabled.
     */
    List<String> getDisableWorlds();

    /**
     * Checks if home overwrite confirmation is required.
     * <p>
     * This method returns a boolean indicating whether the player must confirm
     * with the command /sethomeconfirm to overwrite an existing home.
     *
     * @return true if confirmation is required to overwrite an existing home, false otherwise.
     */
    boolean isHomeOverwriteConfirm();

    /**
     * Checks if home delete confirmation is required.
     * <p>
     * This method returns a boolean indicating whether the player must confirm
     * with the command /delhomeconfirm to delete an existing home.
     *
     * @return true if confirmation is required to delete an existing home, false otherwise.
     */
    boolean isHomeDeleteConfirm();

    /**
     * Gets the default material for the home button.
     * <p>
     * This material is used when the player does not have a home, or when the home
     * material is not set.
     *
     * @return the default material for the home button.
     */
    String getDefaultHomeMaterial();

}
