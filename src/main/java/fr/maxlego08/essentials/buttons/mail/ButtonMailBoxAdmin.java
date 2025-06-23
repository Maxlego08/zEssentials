package fr.maxlego08.essentials.buttons.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ButtonMailBoxAdmin extends ButtonMailBoxHelper {

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
    public void onRender(Player player, InventoryEngine inventory) {

        User user = plugin.getUser(player.getUniqueId());
        if (user == null) return;
        User targetUser = user.getTargetUser();
        if (targetUser == null) return;

        List<MailBoxItem> mailBoxItems = getMailBox(player);
        paginate(mailBoxItems, inventory, (slot, mailBoxItem) -> displayItem(slot, mailBoxItem, player, user, inventory));
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        return !getMailBox(player).isEmpty();
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
