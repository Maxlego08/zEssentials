package fr.maxlego08.essentials.module.modules.trade.inventory;

import fr.maxlego08.essentials.module.modules.trade.TradeModule;
import fr.maxlego08.essentials.module.modules.trade.model.TradePlayer;
import fr.maxlego08.essentials.module.modules.trade.model.TradeSession;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class TradeInventoryHolder implements InventoryHolder {

    private final Player viewer;
    private final TradeSession session;
    private final TradeModule module;
    private final Inventory inventory;

    public TradeInventoryHolder(Player viewer, TradeSession session, TradeModule module) {
        this.viewer = viewer;
        this.session = session;
        this.module = module;
        this.inventory = Bukkit.createInventory(this, 54, "Trade with " + session.getOtherTradePlayer(viewer).getPlayer().getName());
        setupInventory();
    }

    public void open() {
        viewer.openInventory(inventory);
    }

    private void setupInventory() {
        var config = module.getConfiguration();
        var decorations = config.getConfigurationSection("decorations");
        if (decorations != null) {
            for (String key : decorations.getKeys(false)) {
                String path = "decorations." + key;
                ItemStack item = module.getItem(path, viewer);
                List<Integer> slots = config.getIntegerList(path + ".slot");
                for (int slot : slots) {
                    inventory.setItem(slot, item);
                }
            }
        }
        
        updateButtons();
        updateItems();
    }
    
    public void updateItems() {
        TradePlayer me = session.getTradePlayer(viewer);
        TradePlayer other = session.getOtherTradePlayer(viewer);
        
        List<Integer> mySlots = module.getOwnSlots();
        List<ItemStack> myItems = me.getItems();
        for (int i = 0; i < mySlots.size(); i++) {
            int slot = mySlots.get(i);
            if (i < myItems.size()) {
                inventory.setItem(slot, myItems.get(i));
            } else {
                inventory.setItem(slot, null);
            }
        }
        
        List<Integer> otherSlots = module.getPartnerSlots();
        List<ItemStack> otherItems = other.getItems();
        for (int i = 0; i < otherSlots.size(); i++) {
            int slot = otherSlots.get(i);
            if (i < otherItems.size()) {
                inventory.setItem(slot, otherItems.get(i));
            } else {
                inventory.setItem(slot, null);
            }
        }
    }
    
    public void updateButtons() {
        TradePlayer me = session.getTradePlayer(viewer);
        String path = "own.confirm-item." + (me.isReady() ? "cancel" : "accept");
        ItemStack readyBtn = module.getItem(path, viewer);
        inventory.setItem(module.getOwnConfirmSlot(), readyBtn);
        
        TradePlayer other = session.getOtherTradePlayer(viewer);
        String otherPath = "partner.confirm-item." + (other.isReady() ? "cancel" : "accept");
        ItemStack otherBtn = module.getItem(otherPath, viewer, "%partner-name%", other.getPlayer().getName());
        inventory.setItem(module.getPartnerConfirmSlot(), otherBtn);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public TradeSession getSession() {
        return session;
    }
    
    public Player getViewer() {
        return viewer;
    }
}

