package fr.maxlego08.essentials.buttons.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ButtonMailBoxAdmin extends ButtonMailBoxHelper implements PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonMailBoxAdmin(Plugin plugin) {
        super((EssentialsPlugin) plugin);
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;
        User targetUser = user.getTargetUser();
        if (targetUser == null) return;

        List<MailBoxItem> mailBoxItems = getMailBox(player);
        Pagination<MailBoxItem> pagination = new Pagination<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        pagination.paginate(mailBoxItems, this.slots.size(), inventory.getPage()).forEach(mailBoxItem -> displayItem(this.slots.get(atomicInteger.getAndIncrement()), mailBoxItem, player, targetUser, inventory));
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryDefault inventory, Placeholders placeholders) {
        return getMailBox(player).size() > 0;
    }

    @Override
    public int getPaginationSize(Player player) {
        return getMailBox(player).size();
    }

    private List<MailBoxItem> getMailBox(Player player) {
        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return new ArrayList<>();

        User targetUser = user.getTargetUser();
        if (targetUser == null) return new ArrayList<>();

        return targetUser.getMailBoxItems();
    }
}
