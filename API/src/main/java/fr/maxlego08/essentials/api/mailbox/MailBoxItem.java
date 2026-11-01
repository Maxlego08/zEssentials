package fr.maxlego08.essentials.api.mailbox;

import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.menu.api.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

/**
 * Represents an item in a player's mailbox.
 */
public class MailBoxItem {

    private final UUID uuid;
    private final ItemStack itemStack;
    private final Date expiredAt;
    private int id;

    /**
     * Constructs a new MailBoxItem.
     *
     * @param uuid      the UUID of the player who owns the item
     * @param itemStack the item stack stored in the mailbox
     * @param expiredAt the expiration date of the item in the mailbox
     */
    public MailBoxItem(UUID uuid, ItemStack itemStack, Date expiredAt) {
        this.uuid = uuid;
        this.itemStack = itemStack;
        this.expiredAt = expiredAt;
    }

    /**
     * Restores a mailbox item from its stored data using zMenu's item serializer.
     * <p>
     * Requires zMenu 1.1.2.1 or later to be enabled and its {@link InventoryManager}
     * service to be registered. The stored item format remains compatible with
     * mailbox entries saved before the API migration.
     *
     * @param mailBoxDTO the data transfer object containing mailbox item data
     * @throws NullPointerException if the DTO is null or the inventory manager service is unavailable
     * @see InventoryManager#deserializeItemStack(String)
     */
    public MailBoxItem(MailBoxDTO mailBoxDTO) {
        this.id = mailBoxDTO.id();
        this.uuid = mailBoxDTO.unique_id();
        InventoryManager inventoryManager = Bukkit.getServicesManager().load(InventoryManager.class);
        this.itemStack = inventoryManager.deserializeItemStack(mailBoxDTO.itemstack());
        this.expiredAt = mailBoxDTO.expired_at();
    }

    /**
     * Gets the ID of the mailbox item.
     *
     * @return the ID of the mailbox item
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the mailbox item.
     *
     * @param id the new ID of the mailbox item
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the UUID of the player who owns the item.
     *
     * @return the UUID of the player
     */
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * Gets the item stack stored in the mailbox.
     *
     * @return the stored item, or null if no item was stored or deserialization failed
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Gets the expiration date of the item in the mailbox.
     *
     * @return the expiration date
     */
    public Date getExpiredAt() {
        return expiredAt;
    }

    /**
     * Checks if the item in the mailbox has expired.
     *
     * @return true if the item has expired, false otherwise
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiredAt.getTime();
    }
}
