package fr.maxlego08.essentials.module.modules;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.storage.IStorage;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.ZModule;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class MailBoxModule extends ZModule {

    private long expiration;

    public MailBoxModule(ZEssentialsPlugin plugin) {
        super(plugin, "mailbox");
    }

    public void addItem(UUID uuid, ItemStack itemStack) {

        MailBoxItem mailBoxItem = new MailBoxItem(uuid, itemStack, new Date(System.currentTimeMillis() + expiration));

        User user = this.plugin.getUser(uuid);
        if (user != null) {
            user.addMailBoxItem(mailBoxItem);
        } else {
            IStorage iStorage = this.plugin.getStorageManager().getStorage();
            iStorage.addMailBoxItem(mailBoxItem);
        }
    }

}
