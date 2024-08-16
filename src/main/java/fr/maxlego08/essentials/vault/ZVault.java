package fr.maxlego08.essentials.vault;

import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.api.vault.VaultItem;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

public class ZVault extends ZUtils implements Vault {

    private final UUID uniqueId;
    private final int vaultId;
    private final Map<Integer, VaultItem> vaultItems = new HashMap<>();
    private String name;
    private ItemStack iconItemStack;

    public ZVault(UUID uniqueId, int vaultId, String name, ItemStack iconItemStack) {
        this.uniqueId = uniqueId;
        this.vaultId = vaultId;
        this.name = name;
        this.iconItemStack = iconItemStack;
    }

    public ZVault(UUID uuid, int vaultId) {
        this(uuid, vaultId, "Vault-" + vaultId, null);
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public Map<Integer, VaultItem> getVaultItems() {
        return vaultItems;
    }

    @Override
    public int getVaultId() {
        return vaultId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ItemStack getIconItemStack() {
        return iconItemStack;
    }

    @Override
    public void setIconItemStack(ItemStack iconItemStack) {
        this.iconItemStack = iconItemStack;
    }

    @Override
    public void createItem(VaultItemDTO vaultItemDTO) {
        this.vaultItems.put(vaultItemDTO.slot(), new ZVaultItem(vaultItemDTO.slot(), ItemStackUtils.deserializeItemStack(vaultItemDTO.item()), vaultItemDTO.quantity()));
    }

    @Override
    public boolean contains(int slot) {
        return this.vaultItems.containsKey(slot);
    }

    @Override
    public boolean contains(ItemStack itemStack) {
        return this.vaultItems.values().stream().anyMatch(vaultItem -> vaultItem.getItemStack().isSimilar(itemStack));
    }

    @Override
    public Optional<VaultItem> find(ItemStack itemStack) {
        return this.vaultItems.values().stream().filter(vaultItem -> vaultItem.getItemStack().isSimilar(itemStack)).findFirst();
    }

    @Override
    public int getNextSlot() {
        return IntStream.range(0, 45).filter(i -> !this.vaultItems.containsKey(i)).findFirst().orElse(-1);
    }

    @Override
    public boolean hasFreeSlot() {
        return getNextSlot() != -1;
    }

    @Override
    public long getMaterialAmount(Material material) {
        var optional = find(new ItemStack(material));
        return optional.map(VaultItem::getQuantity).orElse(0L);
    }
}
