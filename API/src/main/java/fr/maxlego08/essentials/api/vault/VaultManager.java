package fr.maxlego08.essentials.api.vault;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface VaultManager {

    void loadVaults();

    void openVault(Player player, int vaultId);

    PlayerVaults getPlayerVaults(OfflinePlayer offlinePlayer);

    PlayerVaults getPlayerVaults(UUID uniqueId);

    void setPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot);

    void addPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot);

    VaultResult addVaultItem(Vault vault, UUID uniqueId, ItemStack currentItem, int slot, int quantity, int size);

    VaultResult updateVaultItemQuantity(Vault vault, UUID uniqueId, VaultItem vaultItem, int quantity);

    VaultResult updateExistingVaultItem(Vault currentVault, UUID uniqueId, ItemStack currentItem, int quantity);

    VaultResult addNewItemToVault(Vault vault, UUID uniqueId, ItemStack currentItem, int quantity, int size, int totalSlots, int slot);

    void remove(Vault vault, VaultItem vaultItem, Player player, long amount, int slot);

    String getIconOpen();

    String getIconClose();

    boolean hasPermission(UUID uniqueId, int vaultId);

    List<String> getVaultAsTabCompletion(Player player);

    void openConfiguration(Player player, int vaultId);

    void changeIcon(Player player, Vault vault);

    void resetIcon(Player player, Vault vault);

    void changeName(Player player, Vault vault);

    void resetName(Player player, Vault vault);

    void addItem(UUID uuid, ItemStack itemStack);

    long getMaterialAmount(Player player, Material material);

    void removeMaterial(Player player, Material material, long amountToRemove);
}
