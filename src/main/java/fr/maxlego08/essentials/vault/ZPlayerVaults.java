package fr.maxlego08.essentials.vault;

import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.zutils.utils.ZUtils;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ZPlayerVaults extends ZUtils implements PlayerVaults {

    private final UUID uniqueId;
    private final Map<Integer, Vault> vaults = new HashMap<>();
    private int slots;
    private Vault targetVault;

    public ZPlayerVaults(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public Map<Integer, Vault> getVaults() {
        return vaults;
    }

    @Override
    public void createVault(VaultDTO vaultDTO) {
        this.vaults.put(vaultDTO.vault_id(), new ZVault(vaultDTO.unique_id(), vaultDTO.vault_id(), vaultDTO.name(), ItemStackUtils.deserializeItemStack(vaultDTO.icon())));
    }

    @Override
    public int getSlots() {
        return slots;
    }

    @Override
    public void setSlots(int slots) {
        this.slots = slots;
    }

    @Override
    public Vault getTargetVault() {
        return targetVault;
    }

    @Override
    public void setTargetVault(Vault targetVault) {
        this.targetVault = targetVault;
    }

    @Override
    public Vault getVault(int vaultId) {
        return this.vaults.computeIfAbsent(vaultId, id -> new ZVault(this.uniqueId, id));
    }

    @Override
    public Optional<Vault> find(ItemStack currentItem) {
        return this.vaults.values().stream().filter(vault -> vault.contains(currentItem)).findFirst();
    }

    @Override
    public Vault firstAvailableVault() {
        if (this.vaults.isEmpty()) return getVault(1);
        var optional = this.vaults.values().stream().filter(Vault::hasFreeSlot).findFirst();
        if (optional.isEmpty()) {
            int nextId = this.vaults.values().stream().map(Vault::getVaultId).sorted().findFirst().orElse(0) + 1;
            return getVault(nextId);
        }
        return optional.get();
    }
}
