package fr.maxlego08.essentials.vault;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.dto.PlayerSlotDTO;
import fr.maxlego08.essentials.api.dto.VaultDTO;
import fr.maxlego08.essentials.api.dto.VaultItemDTO;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.vault.PermissionSlotsVault;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.api.vault.VaultItem;
import fr.maxlego08.essentials.api.vault.VaultManager;
import fr.maxlego08.essentials.api.vault.VaultResult;
import fr.maxlego08.essentials.module.ZModule;
import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VaultModule extends ZModule implements VaultManager {

    private final Map<UUID, PlayerVaults> vaults = new HashMap<>();
    private int maxVaults;
    private String iconOpen;
    private String iconClose;
    private String vaultNameRegex;
    private List<PermissionSlotsVault> vaultPermissions;

    public VaultModule(ZEssentialsPlugin plugin) {
        super(plugin, "vault");
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        this.loadInventory("vault");
        this.loadInventory("vault-configuration");
    }

    @Override
    public void loadVaults() {

        var storage = getStorage();
        List<VaultDTO> vaultDTOS = storage.getVaults();
        List<VaultItemDTO> vaultItemDTOS = storage.getVaultItems();
        List<PlayerSlotDTO> slotDTOS = storage.getPlayerVaultSlots();

        // Create Vault
        vaultDTOS.forEach(vaultDTO -> {
            PlayerVaults playerVaults = this.vaults.computeIfAbsent(vaultDTO.unique_id(), ZPlayerVaults::new);
            playerVaults.createVault(vaultDTO);
        });

        // Store Vault Items
        vaultItemDTOS.forEach(vaultItemDTO -> {
            PlayerVaults playerVaults = this.vaults.computeIfAbsent(vaultItemDTO.unique_id(), ZPlayerVaults::new);
            Vault vault = playerVaults.getVaults().computeIfAbsent(vaultItemDTO.vault_id(), id -> new ZVault(vaultItemDTO.unique_id(), id));
            vault.createItem(vaultItemDTO);
        });

        // Store slots
        slotDTOS.forEach(playerSlotDTO -> {
            PlayerVaults playerVaults = this.vaults.computeIfAbsent(playerSlotDTO.unique_id(), ZPlayerVaults::new);
            playerVaults.setSlots(playerSlotDTO.slots());
        });
    }

    @Override
    public void openVault(Player player, int vaultId) {

        if (vaultId < 1 || vaultId > this.maxVaults) {
            message(player, Message.COMMAND_VAULT_NOT_FOUND, "%vaultId%", vaultId);
            return;
        }

        if (!hasPermission(player.getUniqueId(), vaultId)) {
            message(player, Message.COMMAND_VAULT_NO_PERMISSION);
            return;
        }

        PlayerVaults playerVaults = getPlayerVaults(player);
        Vault vault = playerVaults.getVault(vaultId);
        playerVaults.setTargetVault(vault);

        this.plugin.openInventory(player, "vault");
    }

    @Override
    public PlayerVaults getPlayerVaults(OfflinePlayer offlinePlayer) {
        return getPlayerVaults(offlinePlayer.getUniqueId());
    }

    @Override
    public PlayerVaults getPlayerVaults(UUID uniqueId) {
        return this.vaults.computeIfAbsent(uniqueId, ZPlayerVaults::new);
    }

    @Override
    public void setPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot) {

        PlayerVaults playerVaults = getPlayerVaults(offlinePlayer);
        playerVaults.setSlots(slot);

        this.getStorage().setVaultSlot(offlinePlayer.getUniqueId(), slot);

        message(sender, Message.COMMAND_VAULT_SET_SLOT, "%player%", offlinePlayer.getName(), "%slots%", slot);
    }

    @Override
    public int getMaxSlotsPlayer(Player player) {
        return Math.max(getPlayerVaults(player.getUniqueId()).getSlots(),
                this.vaultPermissions.stream()
                        .filter(permission -> player.hasPermission(permission.permission()))
                        .mapToInt(PermissionSlotsVault::slots).max().orElse(0));
    }

    @Override
    public void addPlayerSlot(CommandSender sender, OfflinePlayer offlinePlayer, int slot) {
        PlayerVaults playerVaults = getPlayerVaults(offlinePlayer);
        playerVaults.setSlots(playerVaults.getSlots() + slot);

        this.getStorage().setVaultSlot(offlinePlayer.getUniqueId(), playerVaults.getSlots());

        message(sender, Message.COMMAND_VAULT_ADD_SLOT, "%player%", offlinePlayer.getName(), "%amount%", slot);
    }

    @Override
    public VaultResult addVaultItem(Vault vault, UUID uniqueId, ItemStack currentItem, int slot, int quantity, int size) {

        var playerVault = getPlayerVaults(uniqueId);
        if (slot != -1) {
            VaultItem vaultItem = vault.getVaultItems().get(slot);
            if (vaultItem != null && vaultItem.getItemStack().isSimilar(currentItem)) {
                return updateVaultItemQuantity(vault, uniqueId, vaultItem, quantity);
            }
        }

        Optional<Vault> optionalVault = playerVault.find(currentItem);
        if (optionalVault.isPresent()) {
            return updateExistingVaultItem(optionalVault.get(), uniqueId, currentItem, quantity);
        }

        return addNewItemToVault(vault, uniqueId, currentItem, quantity, size, playerVault.getSlots(), slot);
    }

    @Override
    public VaultResult updateVaultItemQuantity(Vault vault, UUID uniqueId, VaultItem vaultItem, int quantity) {
        vaultItem.addQuantity(quantity);
        this.getStorage().updateVaultQuantity(uniqueId, vault.getVaultId(), vaultItem.getSlot(), vaultItem.getQuantity());
        return new VaultResult(vault, vaultItem.getSlot());
    }

    @Override
    public VaultResult updateExistingVaultItem(Vault currentVault, UUID uniqueId, ItemStack currentItem, int quantity) {
        Optional<VaultItem> optionalVaultItem = currentVault.find(currentItem);
        if (optionalVaultItem.isPresent()) {
            VaultItem currentVaultItem = optionalVaultItem.get();
            currentVaultItem.addQuantity(quantity);
            this.getStorage().updateVaultQuantity(uniqueId, currentVault.getVaultId(), currentVaultItem.getSlot(), currentVaultItem.getQuantity());
            return new VaultResult(currentVault, currentVaultItem.getSlot());
        }
        return null;
    }

    @Override
    public VaultResult addNewItemToVault(Vault vault, UUID uniqueId, ItemStack currentItem, int quantity, int size, int totalSlots, int slot) {
        int nextSlot = vault.getNextSlot() + ((vault.getVaultId() - 1) * size);
        if (nextSlot == -1 || nextSlot >= totalSlots) {
            return null;
        }

        if (slot == -1) slot = vault.getNextSlot();

        VaultItem newVaultItem = new ZVaultItem(slot, currentItem, quantity);
        vault.getVaultItems().put(slot, newVaultItem);
        int finalSlot = slot;
        getStorage().createVaultItem(uniqueId, vault.getVaultId(), finalSlot, newVaultItem.getQuantity(), ItemStackUtils.serializeItemStack(currentItem));
        return new VaultResult(vault, slot);
    }

    @Override
    public void remove(Vault vault, VaultItem vaultItem, Player player, long amount, int slot) {
        if (!vault.contains(slot)) return;

        int freespace = 0;
        var itemStack = vaultItem.getItemStack();

        for (int invSlot = 0; invSlot <= 35; invSlot++) {
            ItemStack invItem = player.getInventory().getItem(invSlot);
            if (invItem == null) {
                freespace += itemStack.getMaxStackSize();
            } else if (invItem.isSimilar(itemStack)) {
                freespace += Math.max(0, itemStack.getMaxStackSize() - invItem.getAmount());
            }
        }

        if (amount == -1) {
            amount = Math.min(freespace, vaultItem.getQuantity());
        } else {
            amount = Math.min(Math.min(amount, vaultItem.getQuantity()), freespace);
        }

        long total = amount / itemStack.getMaxStackSize();
        for (long i = 0; i < total; i++) {
            player.getInventory().addItem(itemStack.asQuantity(itemStack.getMaxStackSize()));
        }

        player.getInventory().addItem(itemStack.asQuantity((int) (amount - total * itemStack.getMaxStackSize())));

        var storage = getStorage();
        if (vaultItem.getQuantity() > amount) {

            vaultItem.removeQuantity(amount);
            storage.updateVaultQuantity(vault.getUniqueId(), vault.getVaultId(), vaultItem.getSlot(), vaultItem.getQuantity());
        } else {

            vault.getVaultItems().remove(slot);
            storage.removeVaultItem(vault.getUniqueId(), vault.getVaultId(), vaultItem.getSlot());
        }
    }

    @Override
    public String getIconOpen() {
        return this.iconOpen;
    }

    @Override
    public String getIconClose() {
        return this.iconClose;
    }

    @Override
    public boolean hasPermission(UUID uniqueId, int vaultId) {
        PlayerVaults playerVaults = this.getPlayerVaults(uniqueId);
        return playerVaults.getSlots() > (vaultId * 45) - 45;
    }

    @Override
    public List<String> getVaultAsTabCompletion(Player player) {
        List<String> strings = IntStream.range(1, this.maxVaults).filter(vaultID -> hasPermission(player.getUniqueId(), vaultID)).mapToObj(String::valueOf).collect(Collectors.toList());
        if (hasPermission(player, Permission.ESSENTIALS_VAULT_ADD_SLOT)) strings.add("add");
        if (hasPermission(player, Permission.ESSENTIALS_VAULT_SET_SLOT)) strings.add("set");
        if (hasPermission(player, Permission.ESSENTIALS_VAULT_GIVE)) strings.add("give");
        return strings;
    }

    @Override
    public void openConfiguration(Player player, int vaultId) {

        if (vaultId < 1 || vaultId > this.maxVaults) {
            message(player, Message.COMMAND_VAULT_NOT_FOUND, "%vaultId%", vaultId);
            return;
        }

        if (!hasPermission(player.getUniqueId(), vaultId)) {
            message(player, Message.COMMAND_VAULT_NO_PERMISSION);
            return;
        }

        PlayerVaults playerVaults = getPlayerVaults(player);
        Vault vault = playerVaults.getVault(vaultId);
        playerVaults.setTargetVault(vault);

        this.plugin.openInventory(player, "vault-configuration");
    }

    @Override
    public void changeIcon(Player player, Vault vault) {

        var itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().isAir()) {
            message(player, Message.COMMAND_VAULT_CHANGE_ICON_ERROR);
            return;
        }

        vault.setIconItemStack(itemStack.clone());
        getStorage().updateVault(player.getUniqueId(), vault);
        message(player, Message.COMMAND_VAULT_CHANGE_ICON_SUCCESS);

        openVault(player, vault.getVaultId());
    }

    @Override
    public void resetIcon(Player player, Vault vault) {

        vault.setIconItemStack(null);
        getStorage().updateVault(player.getUniqueId(), vault);
        message(player, Message.COMMAND_VAULT_RESET_ICON);

        openVault(player, vault.getVaultId());
    }

    @Override
    public void changeName(Player player, Vault vault) {

        this.plugin.startInteractiveChat(player, message -> {

            Pattern pattern = Pattern.compile(this.vaultNameRegex == null ? "^[a-zA-Z0-9_-]{3,16}$" : this.vaultNameRegex);
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {

                vault.setName(message);
                getStorage().updateVault(player.getUniqueId(), vault);
                message(player, Message.COMMAND_VAULT_RENAME_SUCCESS, "%name%", message);

            } else {

                message(player, Message.COMMAND_VAULT_RENAME_ERROR);
            }

            openVault(player, vault.getVaultId());

        }, System.currentTimeMillis() + (60 * 1000));

        player.closeInventory();
        message(player, Message.COMMAND_VAULT_RENAME_START);
    }

    @Override
    public void resetName(Player player, Vault vault) {

        vault.setName("Vault-" + vault.getVaultId());
        getStorage().updateVault(player.getUniqueId(), vault);
        message(player, Message.COMMAND_VAULT_RENAME_RESET);
        openVault(player, vault.getVaultId());
    }

    @Override
    public boolean addItem(UUID uniqueId, ItemStack itemStack) {
        return addItem(uniqueId, itemStack, itemStack.getAmount());
    }

    @Override
    public boolean addItem(UUID uniqueId, ItemStack itemStack, long amount) {
        if (itemStack == null || itemStack.getType().isAir()) return false;

        var storage = getStorage();
        var playerVaults = getPlayerVaults(uniqueId);

        var vault = playerVaults.find(itemStack).orElseGet(playerVaults::firstAvailableVault);

        vault.find(itemStack).ifPresentOrElse(vaultItem -> {
            vaultItem.addQuantity(amount);
            storage.updateVaultQuantity(uniqueId, vault.getVaultId(), vaultItem.getSlot(), vaultItem.getQuantity());
        }, () -> {
            int nextSlot = vault.getNextSlot();
            VaultItem newVaultItem = new ZVaultItem(nextSlot, itemStack, amount);
            vault.getVaultItems().put(nextSlot, newVaultItem);
            storage.createVaultItem(uniqueId, vault.getVaultId(), nextSlot, newVaultItem.getQuantity(), ItemStackUtils.serializeItemStack(itemStack));
        });
        return true;
    }

    @Override
    public long getMaterialAmount(Player player, Material material) {
        PlayerVaults playerVaults = getPlayerVaults(player.getUniqueId());
        return playerVaults.getVaults().values().stream().mapToLong(vault -> vault.getMaterialAmount(material)).sum();
    }

    @Override
    public void removeMaterial(Player player, Material material, long amountToRemove) {
        PlayerVaults playerVaults = getPlayerVaults(player.getUniqueId());
        var itemStack = new ItemStack(material);
        for (Vault vault : playerVaults.getVaults().values()) {
            var optional = vault.find(itemStack);
            if (optional.isPresent()) {
                var vaultItem = optional.get();
                vaultItem.removeQuantity(amountToRemove);
                getStorage().updateVaultQuantity(player.getUniqueId(), vault.getVaultId(), vaultItem.getSlot(), vaultItem.getQuantity());
                return;
            }
        }
    }

    @Override
    public Collection<Material> getMaterials(Player player) {
        Set<Material> materials = new HashSet<>();
        PlayerVaults playerVaults = getPlayerVaults(player.getUniqueId());
        for (Vault vault : playerVaults.getVaults().values()) {
            materials.addAll(vault.getVaultItems().values().stream().map(vaultItem -> vaultItem.getItemStack().getType()).toList());
        }
        return materials;
    }
}
