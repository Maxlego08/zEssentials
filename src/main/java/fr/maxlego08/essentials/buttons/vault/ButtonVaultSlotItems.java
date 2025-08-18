package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.api.vault.VaultItem;
import fr.maxlego08.essentials.api.vault.VaultResult;
import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ButtonVaultSlotItems extends Button {

    private final EssentialsPlugin plugin;

    public ButtonVaultSlotItems(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onInventoryOpen(Player player, InventoryEngine inventory, Placeholders placeholders) {

        PlayerVaults playerVaults = this.plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        boolean editable = vault != null && (vault.getUniqueId().equals(player.getUniqueId()) || player.hasPermission(Permission.ESSENTIALS_VAULT_SHOW_EDIT.asPermission()));
        inventory.setDisablePlayerInventoryClick(!editable);
        if (vault == null) return;

        placeholders.register("vault-name", vault.getName());
        var owner = Bukkit.getOfflinePlayer(vault.getUniqueId());
        placeholders.register("player", owner.getName());
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {

        var manager = this.plugin.getVaultManager();

        PlayerVaults playerVaults = manager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        int startSlot = getStartSlot(player);

        if (startSlot > 0 && startSlot < this.slots.size()) {
            for (int index = 0; index < startSlot; index++) {
                this.releaseSlot(inventory, index);
            }
        }

        vault.getVaultItems().forEach((slot, vaultItem) -> setVaultItem(slot, vault, vaultItem, inventory, player));
    }

    private void setVaultItem(Integer slot, Vault vault, VaultItem vaultItem, InventoryEngine inventory, Player player) {
        ItemStack itemStack = vaultItem.getDisplayItemStack(player, this.plugin.getComponentMessage());
        inventory.addItem(slot, itemStack).setClick(event -> {
            event.setCancelled(true);
            this.onBagClick(player, vault, vaultItem, event.getClick(), slot, inventory, event);
        });
    }

    private void onBagClick(Player player, Vault vault, VaultItem vaultItem, ClickType clickType, int slot, InventoryEngine inventoryDefault, InventoryClickEvent event) {

        var manager = this.plugin.getVaultManager();
        InventoryAction action = event.getAction();
        boolean isPickup = action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_HALF || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME;

        if (!vault.getUniqueId().equals(player.getUniqueId()) && !player.hasPermission(Permission.ESSENTIALS_VAULT_SHOW_EDIT.asPermission())) {
            return;
        }

        if (clickType == ClickType.RIGHT && isPickup) {

            manager.remove(vault, vaultItem, player, 64, slot);
            updateInventoryBag(player, vaultItem, slot, inventoryDefault, vault);
        } else if (clickType == ClickType.LEFT && isPickup) {

            manager.remove(vault, vaultItem, player, 1, slot);
            updateInventoryBag(player, vaultItem, slot, inventoryDefault, vault);
        } else if (clickType == ClickType.SHIFT_LEFT) {

            manager.remove(vault, vaultItem, player, -1, slot);
            updateInventoryBag(player, vaultItem, slot, inventoryDefault, vault);
        }
    }

    private void updateInventoryBag(Player player, VaultItem bagItem, int slot, InventoryEngine inventoryDefault, Vault vault) {
        if (bagItem.getQuantity() <= 0 || !vault.contains(slot)) {
            releaseSlot(inventoryDefault, slot);
        } else
            inventoryDefault.getSpigotInventory().setItem(slot, bagItem.getDisplayItemStack(player, this.plugin.getComponentMessage()));
    }

    protected void releaseSlot(InventoryEngine inventory, int slot) {
        inventory.addItem(slot, new ItemStack(Material.AIR)).setClick(event -> event.setCancelled(true));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event, Player player, InventoryEngine inventoryDefault) {

        var clickType = event.getClick();
        var itemStack = event.getCurrentItem();
        var cursorItemStack = event.getCursor();
        var slot = event.getSlot();
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        var manager = this.plugin.getVaultManager();

        PlayerVaults playerVaults = manager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) {
            event.setCancelled(true);
            return;
        }

        boolean editable = vault.getUniqueId().equals(player.getUniqueId()) || player.hasPermission(Permission.ESSENTIALS_VAULT_SHOW_EDIT.asPermission());
        if (!editable) {
            event.setCancelled(true);
            return;
        }

        if (event.getWhoClicked().getInventory().equals(event.getClickedInventory()) && clickType.isShiftClick() && itemStack != null && itemStack.getType() != Material.AIR) {

            if (!isValidSlot(slot, player)) {
                event.setCancelled(true);
                return;
            }

            VaultResult vaultResult = manager.addVaultItem(vault, player.getUniqueId(), event.getCurrentItem(), -1, itemStack.getAmount(), this.slots.size());

            if (vaultResult == null) {
                event.setCancelled(true);
                return;
            }
            event.setCurrentItem(null);

            if (vaultResult.vault().getVaultId() == vault.getVaultId()) {

                setVaultItem(vaultResult.slot(), vault, vault.getVaultItems().get(vaultResult.slot()), inventoryDefault, player);

            } else {
                manager.openVault(player, vaultResult.vault().getVaultId());
            }

        } else if (topInventory.equals(event.getClickedInventory()) && !cursorItemStack.getType().equals(Material.AIR)) {

            if (!isValidSlot(slot, player)) {
                event.setCancelled(true);
                return;
            }

            VaultItem vaultItem = vault.getVaultItems().get(slot);
            if (vaultItem != null && !vaultItem.getItemStack().isSimilar(cursorItemStack)) {
                return;
            }

            if (event.getClick().equals(ClickType.RIGHT)) {
                VaultResult vaultResult = manager.addVaultItem(vault, player.getUniqueId(), cursorItemStack, slot, 1, this.slots.size());

                if (vaultResult == null) {
                    event.setCancelled(true);
                    return;
                }

                event.setCursor(cursorItemStack.subtract());
                if (vaultResult.vault().getVaultId() == vault.getVaultId()) {

                    setVaultItem(vaultResult.slot(), vault, vault.getVaultItems().get(vaultResult.slot()), inventoryDefault, player);

                } else {
                    manager.openVault(player, vaultResult.vault().getVaultId());
                }

            } else if (event.getClick().equals(ClickType.LEFT)) {
                VaultResult vaultResult = manager.addVaultItem(vault, player.getUniqueId(), cursorItemStack, slot, cursorItemStack.getAmount(), this.slots.size());

                if (vaultResult == null) {
                    event.setCancelled(true);
                    return;
                }

                event.setCursor(null);
                if (vaultResult.vault().getVaultId() == vault.getVaultId()) {

                    setVaultItem(vaultResult.slot(), vault, vault.getVaultItems().get(vaultResult.slot()), inventoryDefault, player);
                } else {

                    manager.openVault(player, vaultResult.vault().getVaultId());
                }
            }
        }
    }

    @Override
    public void onDrag(InventoryDragEvent event, Player player, InventoryEngine inventoryDefault) {
        event.setCancelled(true);
    }

    /**
     * Checks if the given slot is valid for the player's target vault.
     *
     * @param slot   the slot to check
     * @param player the player to check for
     * @return true if the slot is valid, false otherwise
     */
    private boolean isValidSlot(int slot, Player player) {

        int startSlot = getStartSlot(player);

        if (startSlot > 0 && startSlot < this.slots.size()) {
            return slot < startSlot;
        }
        return true;
    }

    /**
     * Returns the start slot for the given player's target vault.
     *
     * @param player the player to get the start slot for
     * @return the start slot for the player's target vault, or 0 if the target vault is null
     */
    private int getStartSlot(Player player) {
        var manager = this.plugin.getVaultManager();
        PlayerVaults playerVaults = manager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return 0;
        int size = this.slots.size();
        return manager.getMaxSlotsPlayer(player) - (size * (vault.getVaultId() - 1));
    }
}
