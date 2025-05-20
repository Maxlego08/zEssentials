package fr.maxlego08.essentials.buttons.mail;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.mailbox.MailBoxItem;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.essentials.module.modules.MailBoxModule;
import fr.maxlego08.essentials.zutils.utils.ComponentMessageHelper;
import fr.maxlego08.essentials.zutils.utils.TimerBuilder;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public abstract class ButtonMailBoxHelper extends PaginateButton {

    private final EssentialsPlugin plugin;

    public ButtonMailBoxHelper(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    protected void displayItem(int slot, MailBoxItem mailBoxItem, Player player, User user, InventoryEngine inventory) {

        MailBoxModule mailBoxModule = this.plugin.getModuleManager().getModule(MailBoxModule.class);

        Placeholders placeholders = new Placeholders();
        placeholders.register("expiration", TimerBuilder.getStringTime(mailBoxItem.getExpiredAt().getTime() - System.currentTimeMillis()));

        ItemStack itemStack = mailBoxItem.getItemStack().clone();
        ComponentMessageHelper.componentMessage.addToLore(itemStack, this.getItemStack().getLore().stream().map(e -> this.plugin.papi(player, e)).collect(Collectors.toList()), placeholders);

        inventory.addItem(slot, itemStack).setClick(event -> mailBoxModule.removeItem(user, player, mailBoxItem));
    }

}
