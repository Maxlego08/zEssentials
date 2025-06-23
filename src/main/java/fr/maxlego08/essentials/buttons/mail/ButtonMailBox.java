package fr.maxlego08.essentials.buttons.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ButtonMailBox extends ButtonMailBoxHelper {

    private final EssentialsPlugin plugin;

    public ButtonMailBox(Plugin plugin) {
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

        List<MailBoxItem> mailBoxItems = user.getMailBoxItems().stream().filter(item -> !item.isExpired()).toList();
        paginate(mailBoxItems, inventory, (slot, mailBoxItem) -> displayItem(slot, mailBoxItem, player, user, inventory));
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {
        User user = plugin.getUser(player.getUniqueId());
        return user != null && !user.getMailBoxItems().isEmpty();
    }

    @Override
    public int getPaginationSize(Player player) {
        User user = plugin.getUser(player.getUniqueId());
        return user == null ? 0 : user.getMailBoxItems().size();
    }
}
