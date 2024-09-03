package fr.maxlego08.essentials.api.worldedit;

import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for managing WorldEdit operations within the plugin.
 * Provides methods to handle WorldEdit items, block editing actions, and user configurations.
 */
public interface WorldeditManager {

    /**
     * Retrieves a WorldEdit item by its name.
     *
     * @param name the name of the WorldEdit item
     * @return an Optional containing the WorldEdit item if found, otherwise empty
     */
    Optional<WorldEditItem> getWorldeditItem(String name);

    /**
     * Gives a WorldEdit item to a player.
     *
     * @param sender   the command sender giving the item
     * @param player   the player to receive the item
     * @param itemName the name of the item to give
     */
    void give(CommandSender sender, Player player, String itemName);

    /**
     * Retrieves a list of all available WorldEdit items.
     *
     * @return a list of WorldEdit item names
     */
    List<String> getWorldeditItems();

    /**
     * Gets a list of allowed materials for a player.
     *
     * @param player the player whose allowed materials are being queried
     * @return a list of allowed materials
     */
    List<String> getAllowedMaterials(Player player);

    /**
     * Checks if a material is on the blacklist.
     *
     * @param material the material to check
     * @return true if the material is blacklisted, false otherwise
     */
    boolean isBlacklist(Material material);

    /**
     * Sets blocks for the specified user according to the provided material percentages.
     *
     * @param user            the user performing the operation
     * @param materialPercents the list of materials and their respective percentages
     */
    void setBlocks(User user, List<MaterialPercent> materialPercents);

    /**
     * Fills blocks for the specified user according to the provided material percentages.
     *
     * @param user            the user performing the operation
     * @param materialPercents the list of materials and their respective percentages
     */
    void fillBlocks(User user, List<MaterialPercent> materialPercents);

    /**
     * Cuts blocks for the specified user.
     *
     * @param user the user performing the operation
     */
    void cutBlocks(User user);

    /**
     * Retrieves the price of a material.
     *
     * @param material the material whose price is being queried
     * @return the price of the material
     */
    BigDecimal getMaterialPrice(Material material);

    /**
     * Confirms a pending action for the specified user.
     *
     * @param user the user confirming the action
     */
    void confirmAction(User user);

    /**
     * Gets the number of blocks per second the player can handle.
     *
     * @param player the player being queried
     * @return the number of blocks per second
     */
    int getBlocksPerSecond(Player player);

    /**
     * Gets the maximum number of blocks the player can handle.
     *
     * @param player the player being queried
     * @return the maximum number of blocks
     */
    int getMaxBlocks(Player player);

    /**
     * Gets the maximum distance for WorldEdit operations for the player.
     *
     * @param player the player being queried
     * @return the maximum distance
     */
    int getMaxDistance(Player player);

    /**
     * Gets the radius for sphere operations for the player.
     *
     * @param player the player being queried
     * @return the sphere radius
     */
    int getSphereRadius(Player player);

    /**
     * Gets the radius for cylinder operations for the player.
     *
     * @param player the player being queried
     * @return the cylinder radius
     */
    int getCylinderRadius(Player player);

    /**
     * Gets the height for sphere operations for the player.
     *
     * @param player the player being queried
     * @return the sphere height
     */
    int getSphereHeight(Player player);

    /**
     * Gets the height for cylinder operations for the player.
     *
     * @param player the player being queried
     * @return the cylinder height
     */
    int getCylinderHeight(Player player);

    /**
     * Sends a message to the user indicating the completion of an operation.
     *
     * @param user the user receiving the message
     */
    void sendFinishMessage(User user);

    /**
     * Gets the batch size for block operations.
     *
     * @return the batch size
     */
    int getBatchSize();

    /**
     * Stops the current editing operation for the user.
     *
     * @param user the user whose editing operation is being stopped
     */
    void stopEdition(User user);

    /**
     * Sends a refund message to the player with the details of refunded materials.
     *
     * @param player         the player receiving the refund
     * @param refundMaterials a map of materials and their refunded amounts
     * @param refundPrice    the total refund price
     * @param economy        the economy system being used
     */
    void sendRefundMessage(Player player, Map<Material, Long> refundMaterials, BigDecimal refundPrice, Economy economy);

    /**
     * Creates walls using the specified materials for the user.
     *
     * @param user            the user performing the operation
     * @param materialPercents the list of materials and their respective percentages
     */
    void wallsBlocks(User user, List<MaterialPercent> materialPercents);

    /**
     * Creates a sphere using the specified materials for the user.
     *
     * @param user            the user performing the operation
     * @param materialPercents the list of materials and their respective percentages
     * @param radius          the radius of the sphere
     * @param filled          whether the sphere is filled or hollow
     */
    void sphereBlocks(User user, List<MaterialPercent> materialPercents, int radius, boolean filled);

    /**
     * Creates a cylinder using the specified materials for the user.
     *
     * @param user            the user performing the operation
     * @param materialPercents the list of materials and their respective percentages
     * @param radius          the radius of the cylinder
     * @param filled          whether the cylinder is filled or hollow
     * @param height          the height of the cylinder
     */
    void cylBlocks(User user, List<MaterialPercent> materialPercents, int radius, boolean filled, int height);

    /**
     * Sets the first position for WorldEdit operations for the player.
     *
     * @param player   the player setting the position
     * @param location the location being set as position 1
     */
    void setPos1(Player player, Location location);

    /**
     * Sets the second position for WorldEdit operations for the player.
     *
     * @param player   the player setting the position
     * @param location the location being set as position 2
     */
    void setPos2(Player player, Location location);

    /**
     * Toggles the option inventory for the user.
     *
     * @param user the user toggling the option
     */
    void toggleOptionInventory(User user);

    /**
     * Toggles the option boss bar for the user.
     *
     * @param user the user toggling the option
     */
    void toggleOptionBossBar(User user);

    /**
     * Gets the WorldEdit boss bar instance.
     *
     * @return the WorldEdit boss bar
     */
    WorldeditBossBar getWorldeditBar();

    /**
     * Gets the WorldEdit boss bar configuration.
     *
     * @return the WorldEdit boss bar configuration
     */
    WorldeditBossBarConfiguration getWorldeditConfiguration();
}
