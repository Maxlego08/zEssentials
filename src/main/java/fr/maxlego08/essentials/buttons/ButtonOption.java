package fr.maxlego08.essentials.buttons;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.user.Option;
import fr.maxlego08.essentials.api.user.User;
import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ButtonOption extends ZButton {

    private final EssentialsPlugin plugin;
    private final Option option;
    private final List<Integer> offsetSlots;
    private final MenuItemStack enableItemStack;
    private final MenuItemStack disableItemStack;
    private final MenuItemStack offsetEnableItemStack;
    private final MenuItemStack offsetDisableItemStack;

    public ButtonOption(EssentialsPlugin plugin, Option option, List<Integer> offsetSlots, MenuItemStack enableItemStack, MenuItemStack disableItemStack, MenuItemStack offsetEnableItemStack, MenuItemStack offsetDisableItemStack) {
        this.plugin = plugin;
        this.option = option;
        this.offsetSlots = offsetSlots;
        this.enableItemStack = enableItemStack;
        this.disableItemStack = disableItemStack;
        this.offsetEnableItemStack = offsetEnableItemStack;
        this.offsetDisableItemStack = offsetDisableItemStack;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        var user = this.plugin.getUser(player.getUniqueId());
        boolean isActive = user.getOption(option);
        var itemStack = isActive ? enableItemStack : disableItemStack;

        for (Integer slot : this.getSlots()) {

            inventory.addItem(slot, itemStack.build(player, false)).setClick(event -> toggleOption(player, inventory, slot, event, user, isActive));

            for (Integer offsetSlot : this.offsetSlots) {
                var offsetItemStack = isActive ? offsetEnableItemStack : offsetDisableItemStack;
                inventory.addItem(slot + offsetSlot, offsetItemStack.build(player, false)).setClick(event -> toggleOption(player, inventory, slot, event, user, isActive));
            }
        }
    }

    private void toggleOption(Player player, InventoryDefault inventory, Integer slot, InventoryClickEvent event, User user, boolean isActive) {
        super.onClick(player, event, inventory, slot, new Placeholders());
        if (option == Option.DISABLE_SCOREBOARD) {
            this.plugin.getScoreboardManager().toggleScoreboard(player, true);
        } else {
            user.setOption(option, !isActive);
        }
        onRender(player, inventory);
    }
}
