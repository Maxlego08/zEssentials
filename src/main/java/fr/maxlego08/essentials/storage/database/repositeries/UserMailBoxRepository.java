package fr.maxlego08.essentials.storage.database.repositeries;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.storage.database.Repository;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.sarah.DatabaseConnection;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserMailBoxRepository extends Repository {
    public UserMailBoxRepository(EssentialsPlugin plugin, DatabaseConnection connection) {
        super(plugin, connection, "user_mail_boxes");
    }

    public List<MailBoxDTO> select(UUID uuid) {
        return this.select(MailBoxDTO.class, table -> table.where("unique_id", uuid));
    }

    public void insert(MailBoxItem mailBoxItem) {
        this.insert(table -> {
            table.uuid("unique_id", mailBoxItem.getUniqueId());
            table.string("itemstack", ItemStackUtils.serializeItemStack(mailBoxItem.getItemStack()));
            table.object("expired_at", mailBoxItem.getExpiredAt());
        }, mailBoxItem::setId);
    }

    public void delete(int id) {
        this.delete(table -> table.where("id", id));
    }

    public void deleteExpiredItems() {
        delete(table -> table.where("expired_at", "<", new Date()));
    }

    public void clear(UUID uuid) {
        delete(table -> table.where("unique_id", uuid));
    }
}
