package fr.maxlego08.essentials.api.vault;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Interface for managing player vaults in the system.
 */
public interface VaultManager {

    /**
     * Loads all vaults from the storage or configuration.
     */
    void loadVaults();

    /**
     * Opens a specific vault for a player.
     *
     * @param player  the player opening the vault
     * @param vaultId the ID of the vault to open
     */
    void openVault(Player player, int vaultId);

    /**
     * Gets the vaults associated with an offline player.
     *
     * @param offlinePlayer the offline player
     * @return the player's vaults
     */
    PlayerVaults getPlayerVaults(OfflinePlayer offlinePlayer);

    /**
     * Gets the vaults associated with a player by their UUID.
     *
     * @param uniqueId the player's UUID
     * @return the player's vaults
     */
    PlayerVaults getPlayerVaults(UUID uniqueId);

    /**
     * Sets the number of slots available to a player in their vaults.
     *
     * @param sender        the sender of the command
     * @param offlinePlayer the player whose slots are being set
     * @param slot          the number of slots to set
     */
    void setPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot);

    /**
     * Gets the maximum number of slots available to a player.
     *
     * @param player the player
     * @return the maximum number of slots
     */
    int getMaxSlotsPlayer(Player player);

    /**
     * Adds a number of slots to a player's vaults.
     *
     * @param sender        the sender of the command
     * @param offlinePlayer the player whose slots are being added
     * @param slot          the number of slots to add
     */
    void addPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot);

    /**
     * Adds an item to a vault.
     *
     * @param vault       the vault to add the item to
     * @param uniqueId    the UUID of the player adding the item
     * @param currentItem the item being added
     * @param slot        the slot in which to add the item
     * @param quantity    the quantity of the item to add
     * @param size        the size of the item stack
     * @return the result of the operation
     */
    VaultResult addVaultItem(Vault vault, UUID uniqueId, ItemStack currentItem, int slot, int quantity, int size);

    /**
     * Updates the quantity of an existing item in a vault.
     *
     * @param vault     the vault containing the item
     * @param uniqueId  the UUID of the player
     * @param vaultItem the item whose quantity is being updated
     * @param quantity  the new quantity of the item
     * @return the result of the operation
     */
    VaultResult updateVaultItemQuantity(Vault vault, UUID uniqueId, VaultItem vaultItem, int quantity);

    /**
     * Updates an existing item in the vault.
     *
     * @param currentVault the vault containing the item
     * @param uniqueId     the UUID of the player
     * @param currentItem  the item being updated
     * @param quantity     the new quantity of the item
     * @return the result of the operation
     */
    VaultResult updateExistingVaultItem(Vault currentVault, UUID uniqueId, ItemStack currentItem, int quantity);

    /**
     * Adds a new item to the vault.
     *
     * @param vault       the vault to add the item to
     * @param uniqueId    the UUID of the player adding the item
     * @param currentItem the item being added
     * @param quantity    the quantity of the item to add
     * @param size        the size of the item stack
     * @param totalSlots  the total number of slots in the vault
     * @param slot        the slot in which to add the item
     * @return the result of the operation
     */
    VaultResult addNewItemToVault(Vault vault, UUID uniqueId, ItemStack currentItem, int quantity, int size, int totalSlots, int slot);

    /**
     * Removes an item from a vault.
     *
     * @param vault     the vault to remove the item from
     * @param vaultItem the item being removed
     * @param player    the player performing the removal
     * @param amount    the amount of the item to remove
     * @param slot      the slot from which to remove the item
     */
    void remove(Vault vault, VaultItem vaultItem, Player player, long amount, int slot);

    /**
     * Gets the open icon for the vault interface.
     *
     * @return the open icon as a string
     */
    String getIconOpen();

    /**
     * Gets the close icon for the vault interface.
     *
     * @return the close icon as a string
     */
    String getIconClose();

    /**
     * Checks if a player has permission to access a specific vault.
     *
     * @param uniqueId the UUID of the player
     * @param vaultId  the ID of the vault
     * @return true if the player has permission, false otherwise
     */
    boolean hasPermission(UUID uniqueId, int vaultId);

    /**
     * Gets a list of vaults available for tab completion for a player.
     *
     * @param player the player
     * @return a list of vault names for tab completion
     */
    List<String> getVaultAsTabCompletion(Player player);

    /**
     * Opens the configuration interface for a vault.
     *
     * @param player  the player opening the configuration
     * @param vaultId the ID of the vault to configure
     */
    void openConfiguration(Player player, int vaultId);

    /**
     * Changes the icon of a vault.
     *
     * @param player the player changing the icon
     * @param vault  the vault whose icon is being changed
     */
    void changeIcon(Player player, Vault vault);

    /**
     * Resets the icon of a vault to its default state.
     *
     * @param player the player resetting the icon
     * @param vault  the vault whose icon is being reset
     */
    void resetIcon(Player player, Vault vault);

    /**
     * Changes the name of a vault.
     *
     * @param player the player changing the name
     * @param vault  the vault whose name is being changed
     */
    void changeName(Player player, Vault vault);

    /**
     * Resets the name of a vault to its default state.
     *
     * @param player the player resetting the name
     * @param vault  the vault whose name is being reset
     */
    void resetName(Player player, Vault vault);

    /**
     * Adds an item to a player's vault by UUID.
     *
     * @param uuid      the UUID of the player
     * @param itemStack the item stack to add
     * @return true if the item was added successfully, false otherwise
     */
    boolean addItem(UUID uuid, ItemStack itemStack);

    /**
     * Adds a specified amount of an item to a player's vault by UUID.
     *
     * @param uuid      the UUID of the player
     * @param itemStack the item stack to add
     * @param amount    the amount of the item to add
     * @return true if the item was added successfully, false otherwise
     */
    boolean addItem(UUID uuid, ItemStack itemStack, long amount);

    /**
     * Gets the total amount of a specific material stored in a player's vaults.
     *
     * @param player   the player
     * @param material the material to count
     * @return the total amount of the material
     */
    long getMaterialAmount(Player player, Material material);

    /**
     * Removes a specified amount of a material from a player's vaults.
     *
     * @param player         the player
     * @param material       the material to remove
     * @param amountToRemove the amount of the material to remove
     */
    void removeMaterial(Player player, Material material, long amountToRemove);

    /**
     * Gets a collection of all materials stored in a player's vaults.
     *
     * @param player the player
     * @return a collection of materials
     */
    Collection<Material> getMaterials(Player player);
}
