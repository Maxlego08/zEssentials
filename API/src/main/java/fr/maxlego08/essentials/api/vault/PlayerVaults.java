package fr.maxlego08.essentials.api.vault;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import fr.maxlego08.essentials.api.dto.VaultDTO;
import org.bukkit.inventory.ItemStack;

public interface PlayerVaults {

    UUID getUniqueId();

    Map<Integer, Vault> getVaults();

    void createVault(VaultDTO vaultDTO);

    int getSlots();

    void setSlots(int slots);

    Vault getTargetVault();

    void setTargetVault(Vault targetVault);

    Vault getVault(int vaultId);

    Optional<Vault> find(ItemStack currentItem);
}
