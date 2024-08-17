package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a vault that holds items for a specific player.
 */
public interface Vault {

    /**
     * Gets the unique identifier of the vault owner.
     *
     * @return the owner's UUID
     */
    UUID getUniqueId();

    /**
     * Gets the map of items stored in the vault.
     *
     * @return a map of vault items where the key is the slot number and the value is the VaultItem object
     */
    Map<Integer, VaultItem> getVaultItems();

    /**
     * Gets the ID of the vault.
     *
     * @return the vault ID
     */
    int getVaultId();

    /**
     * Gets the name of the vault.
     *
     * @return the vault name
     */
    String getName();

    /**
     * Sets the name of the vault.
     *
     * @param name the name to set for the vault
     */
    void setName(String name);

    /**
     * Gets the icon item stack representing the vault.
     *
     * @return the icon item stack
     */
    ItemStack getIconItemStack();

    /**
     * Sets the icon item stack representing the vault.
     *
     * @param iconItemStack the icon item stack to set
     */
    void setIconItemStack(ItemStack iconItemStack);

    /**
     * Creates a new item in the vault using the provided data transfer object.
     *
     * @param vaultItemDTO the data transfer object containing the vault item's creation details
     */
    void createItem(VaultItemDTO vaultItemDTO);

    /**
     * Checks if the vault contains an item at the specified slot.
     *
     * @param slot the slot to check
     * @return true if the vault contains an item at the specified slot, false otherwise
     */
    boolean contains(int slot);

    /**
     * Checks if the vault contains the specified item stack.
     *
     * @param itemStack the item stack to check
     * @return true if the vault contains the item stack, false otherwise
     */
    boolean contains(ItemStack itemStack);

    /**
     * Finds a vault item that matches the specified item stack.
     *
     * @param itemStack the item stack to find
     * @return an optional containing the matching vault item if found, or an empty optional if not
     */
    Optional<VaultItem> find(ItemStack itemStack);

    /**
     * Gets the next available slot in the vault.
     *
     * @return the next available slot number
     */
    int getNextSlot();

    /**
     * Checks if the vault has any free slots.
     *
     * @return true if the vault has free slots, false otherwise
     */
    boolean hasFreeSlot();

    /**
     * Gets the total amount of a specific material stored in the vault.
     *
     * @param material the material to count
     * @return the total amount of the material in the vault
     */
    long getMaterialAmount(Material material);
}
