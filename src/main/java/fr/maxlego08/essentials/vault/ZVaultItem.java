package fr.maxlego08.essentials.vault;

import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.utils.component.AdventureComponent;
import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import fr.maxlego08.essentials.api.vault.VaultItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ZVaultItem implements VaultItem {

    private final int slot;
    private final ItemStack itemStack;
    private long quantity;

    public ZVaultItem(int slot, ItemStack itemStack, long quantity) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.quantity = quantity;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public long getQuantity() {
        return quantity;
    }

    public ItemStack getDisplayItemStack(ComponentMessage componentMessage) {
        ItemStack itemStack = this.itemStack.clone();

        if (componentMessage instanceof AdventureComponent adventureComponent) {

            ItemMeta itemMeta = itemStack.getItemMeta();

            List<Component> lore = itemMeta.hasLore() ? itemMeta.lore() : new ArrayList<>();
            if (lore == null) lore = new ArrayList<>();
            for (String message : Message.VAULT_LORE.getMessageAsStringList()) {
                message = message.replace("%quantity%", String.valueOf(this.quantity));
                lore.add(adventureComponent.getComponent(message).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }

            itemMeta.lore(lore);
            itemStack.setItemMeta(itemMeta);

        }
        return itemStack;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void removeQuantity(long amount) {
        this.quantity -= amount;
    }
}
