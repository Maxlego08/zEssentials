package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.dto.VaultDTO;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a collection of vaults associated with a player.
 */
public interface PlayerVaults {

    /**
     * Gets the unique identifier of the player.
     *
     * @return the player's UUID
     */
    UUID getUniqueId();

    /**
     * Gets the map of vaults owned by the player.
     *
     * @return a map of vaults where the key is the vault ID and the value is the Vault object
     */
    Map<Integer, Vault> getVaults();

    /**
     * Creates a new vault using the provided data transfer object.
     *
     * @param vaultDTO the data transfer object containing the vault's creation details
     */
    void createVault(VaultDTO vaultDTO);

    /**
     * Gets the number of slots available for the player.
     *
     * @return the number of slots
     */
    int getSlots();

    /**
     * Sets the number of slots available for the player.
     *
     * @param slots the number of slots to set
     */
    void setSlots(int slots);

    /**
     * Gets the vault currently targeted by the player.
     *
     * @return the target vault
     */
    Vault getTargetVault();

    /**
     * Sets the vault currently targeted by the player.
     *
     * @param targetVault the target vault to set
     */
    void setTargetVault(Vault targetVault);

    /**
     * Gets a specific vault by its ID.
     *
     * @param vaultId the ID of the vault to retrieve
     * @return the vault with the specified ID
     */
    Vault getVault(int vaultId);

    /**
     * Finds a vault that contains the specified item.
     *
     * @param currentItem the item to find in the vaults
     * @return an optional containing the vault if found, or an empty optional if not
     */
    Optional<Vault> find(ItemStack currentItem);

    /**
     * Finds the first available vault.
     *
     * @return the first available vault
     */
    Vault firstAvailableVault();
}

