package fr.maxlego08.essentials.api.mailbox;

import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
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
     * Constructs a new MailBoxItem from a MailBoxDTO.
     *
     * @param mailBoxDTO the data transfer object containing mailbox item data
     */
    public MailBoxItem(MailBoxDTO mailBoxDTO) {
        this.id = mailBoxDTO.id();
        this.uuid = mailBoxDTO.unique_id();
        this.itemStack = ItemStackUtils.deserializeItemStack(mailBoxDTO.itemstack());
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
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the item stack stored in the mailbox.
     *
     * @return the item stack
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
