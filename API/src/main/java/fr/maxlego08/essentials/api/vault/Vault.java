package fr.maxlego08.essentials.api.vault;

import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Vault {

    UUID getUniqueId();

    Map<Integer, VaultItem> getVaultItems();

    int getVaultId();

    String getName();

    void setName(String name);

    ItemStack getIconItemStack();

    void setIconItemStack(ItemStack iconItemStack);

    void createItem(VaultItemDTO vaultItemDTO);

    boolean contains(int slot);

    boolean contains(ItemStack itemStack);

    Optional<VaultItem> find(ItemStack itemStack);

    int getNextSlot();

    boolean hasFreeSlot();
}
