package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an item stored in a vault.
 */
public interface VaultItem {

    /**
     * Gets the slot in which the item is stored within the vault.
     *
     * @return the slot number
     */
    int getSlot();

    /**
     * Gets the ItemStack representing the item.
     *
     * @return the ItemStack
     */
    ItemStack getItemStack();

    /**
     * Gets the quantity of the item stored in the vault.
     *
     * @return the quantity of the item
     */
    long getQuantity();

    /**
     * Gets the display item stack for showing to players, potentially with additional
     * information provided by a component message.
     *
     * @param adventureComponent the component message to apply to the display item
     * @return the display ItemStack
     */
    ItemStack getDisplayItemStack(ComponentMessage adventureComponent);

    /**
     * Adds a specified amount to the quantity of the item.
     *
     * @param amount the amount to add
     */
    void addQuantity(long amount);

    /**
     * Removes a specified amount from the quantity of the item.
     *
     * @param amount the amount to remove
     */
    void removeQuantity(long amount);
}
