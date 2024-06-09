package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.database.dto.MailBoxDTO;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.essentials.user.ZUser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MailBoxModule extends ZModule {

    private long expiration;

    public MailBoxModule(ZEssentialsPlugin plugin) {
        super(plugin, "mailbox");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("mailbox");
        this.loadInventory("mailbox_admin");
    }

    public void addItem(UUID uuid, ItemStack itemStack) {

        MailBoxItem mailBoxItem = new MailBoxItem(uuid, itemStack, new Date(System.currentTimeMillis() + (this.expiration * 1000)));

        User user = this.plugin.getUser(uuid);
        if (user != null) {

            user.addMailBoxItem(mailBoxItem);
            message(user, Message.MAILBOX_ADD);
        } else {

            IStorage iStorage = this.plugin.getStorageManager().getStorage();
            iStorage.addMailBoxItem(mailBoxItem);
        }
    }

    public void openMailBox(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "mailbox");
    }

    public void openMailBoxAdmin(Player player) {
        this.plugin.getInventoryManager().openInventory(player, this.plugin, "mailbox_admin");
    }

    public void removeItem(User user, Player player, MailBoxItem mailBoxItem) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        PlayerInventory inventory = player.getInventory();

        int firstEmptySlot = inventory.firstEmpty();
        if (firstEmptySlot == -1) {
            message(player, Message.MAILBOX_REMOVE_FULL);
            return;
        }

        List<MailBoxItem> mailBoxItems = user.getMailBoxItems();

        if (mailBoxItem.isExpired()) {
            message(player, Message.MAILBOX_REMOVE_EXPIRE);
            openMailBox(player);

            mailBoxItems.remove(mailBoxItem);
            iStorage.removeMailBoxItem(mailBoxItem.getId());
            return;
        }

        if (mailBoxItems.contains(mailBoxItem)) {

            inventory.addItem(mailBoxItem.getItemStack());

            mailBoxItems.remove(mailBoxItem);
            iStorage.removeMailBoxItem(mailBoxItem.getId());
        }

        if (user.getUniqueId().equals(player.getUniqueId())) {
            openMailBox(player);
        } else {
            openMailBoxAdmin(player);
        }
    }

    public void openMailBox(User user, UUID uuid, String username) {

        IStorage iStorage = this.plugin.getStorageManager().getStorage();
        List<MailBoxDTO> mailBoxDTOS = iStorage.getMailBox(uuid);
        User fakeUser = ZUser.fakeUser(this.plugin, uuid, username);
        fakeUser.setMailBoxItems(mailBoxDTOS);

        user.setTargetUser(fakeUser);
        openMailBoxAdmin(user.getPlayer());
    }
}
