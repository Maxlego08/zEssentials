package fr.maxlego08.essentials.api.mailbox;

import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class MailBoxItem {

    private final UUID uuid;
    private final ItemStack itemStack;
    private final Date expiredAt;
    private int id;

    public MailBoxItem(UUID uuid, ItemStack itemStack, Date expiredAt) {
        this.uuid = uuid;
        this.itemStack = itemStack;
        this.expiredAt = expiredAt;
    }

    public MailBoxItem(MailBoxDTO mailBoxDTO) {
        this.id = mailBoxDTO.id();
        this.uuid = mailBoxDTO.unique_id();
        this.itemStack = ItemStackUtils.deserializeItemStack(mailBoxDTO.itemstack());
        this.expiredAt = mailBoxDTO.expired_at();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expiredAt.getTime();
    }
}
