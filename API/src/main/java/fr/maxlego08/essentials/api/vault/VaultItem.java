package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.utils.component.ComponentMessage;
import org.bukkit.inventory.ItemStack;

public interface VaultItem {

    int getSlot();

    ItemStack getItemStack();

    long getQuantity();

    ItemStack getDisplayItemStack(ComponentMessage adventureComponent);

    void addQuantity(long amount);

    void removeQuantity(long amount);
}
