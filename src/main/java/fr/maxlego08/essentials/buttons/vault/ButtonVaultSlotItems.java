package fr.maxlego08.essentials.buttons.vault;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.vault.PlayerVaults;
import fr.maxlego08.essentials.api.vault.Vault;
import fr.maxlego08.essentials.api.vault.VaultItem;
import fr.maxlego08.essentials.api.vault.VaultResult;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ButtonVaultSlotItems extends ZButton {

    private final EssentialsPlugin plugin;

    public ButtonVaultSlotItems(Plugin plugin) {
        this.plugin = (EssentialsPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onInventoryOpen(Player player, InventoryDefault inventory, Placeholders placeholders) {

        inventory.setDisablePlayerInventoryClick(false);

        PlayerVaults playerVaults = this.plugin.getVaultManager().getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        placeholders.register("vault-name", vault.getName());
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        var manager = this.plugin.getVaultManager();

        PlayerVaults playerVaults = manager.getPlayerVaults(player);
        Vault vault = playerVaults.getTargetVault();
        if (vault == null) return;

        int size = this.slots.size();

        var slots = manager.getMaxSlotsPlayer(player);
        int startSlot = slots - (this.slots.size() * (slots - 1));
        if (startSlot > 0 && startSlot < size) {
            for (int index = 0; index < startSlot; index++) this.releaseSlot(inventory, index);
        }

        vault.getVaultItems().forEach((slot, vaultItem) -> setVaultItem(slot, vault, vaultItem, inventory, player));
    }

    private void setVaultItem(Integer slot, Vault vault, VaultItem vaultItem, InventoryDefault inventory, Player player) {
        ItemStack itemStack = vaultItem.getDisplayItemStack(player, this.plugin.getComponentMessage());
        inventory.addItem(slot, itemStack).setClick(event -> {
            event.setCancelled(true);
            this.onBagClick(player, vault, vaultItem, event.getClick(), slot, inventory, event);
        });
    }

    private void onBagClick(Player player, Vault vault, VaultItem vaultItem, ClickType clickType, int slot, InventoryDefault inventoryDefault, InventoryClickEvent event) {

        var manager = this.plugin.getVaultManager();
        InventoryAction action = event.getAction();
        boolean isPickup = action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_HALF || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME;

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

    private void updateInventoryBag(Player player, VaultItem bagItem, int slot, InventoryDefault inventoryDefault, Vault vault) {
        if (bagItem.getQuantity() <= 0 || !vault.contains(slot)) {
            releaseSlot(inventoryDefault, slot);
        } else
            inventoryDefault.getSpigotInventory().setItem(slot, bagItem.getDisplayItemStack(player, this.plugin.getComponentMessage()));
    }

    protected void releaseSlot(InventoryDefault inventory, int slot) {
        inventory.addItem(slot, new ItemStack(Material.AIR)).setClick(event -> event.setCancelled(true));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event, Player player, InventoryDefault inventoryDefault) {

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


        if (event.getWhoClicked().getInventory().equals(event.getClickedInventory()) && clickType.isShiftClick() && itemStack != null && itemStack.getType() != Material.AIR) {

            if (slot >= 45) {
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
                plugin.getVaultManager().openVault(player, vaultResult.vault().getVaultId());
            }

        } else if (topInventory.equals(event.getClickedInventory()) && !cursorItemStack.getType().equals(Material.AIR)) {

            if (slot >= 45) {
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
    public void onDrag(InventoryDragEvent event, Player player, InventoryDefault inventoryDefault) {
        event.setCancelled(true);
    }
}
