package fr.maxlego08.essentials.module.modules.trade.listeners;

import fr.maxlego08.essentials.api.event.events.trade.TradeCancelEvent;
import fr.maxlego08.essentials.api.event.events.trade.TradeCompleteEvent;
import fr.maxlego08.essentials.module.modules.trade.TradeModule;
import fr.maxlego08.essentials.module.modules.trade.inventory.TradeInventoryHolder;
import fr.maxlego08.essentials.module.modules.trade.model.TradePlayer;
import fr.maxlego08.essentials.module.modules.trade.model.TradeSession;
import fr.maxlego08.essentials.module.modules.trade.enums.TradeState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class TradeInventoryListener implements Listener {

    private final TradeModule module;
    
    public TradeInventoryListener(TradeModule module) {
        this.module = module;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof TradeInventoryHolder holder)) return;

        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        TradeSession session = holder.getSession();
        TradePlayer me = session.getTradePlayer(player);
        
        if (session.getState() == TradeState.COMPLETED) return;

        int slot = event.getRawSlot();
        
        if (slot >= 54) {
            event.setCancelled(false);
            if (event.isShiftClick()) {
                event.setCancelled(true);
                ItemStack item = event.getCurrentItem();
                if (item != null && item.getType() != Material.AIR) {
                    if (me.isReady()) {
                        module.sendMessage(player, "trade-not-ready");
                        me.setReady(false);
                        session.getOtherTradePlayer(player).setReady(false);
                        updateInventories(session);
                    }
                    
                    me.getItems().add(item.clone());
                    event.setCurrentItem(null);
                    updateInventories(session);
                }
            }
            return;
        }

        if (module.getOwnSlots().contains(slot)) {
            if (me.isReady()) {
                me.setReady(false);
                session.getOtherTradePlayer(player).setReady(false);
                module.sendMessage(player, "trade-not-ready");
                updateInventories(session);
                return;
            }
            
            event.setCancelled(false);

            module.getPlugin().getScheduler().runNextTick(task -> {
                updateMyItems(me, holder.getInventory());
                updateInventories(session);
            });
        }
        
        if (slot == module.getOwnConfirmSlot()) {
            if (me.isReady()) {
                me.setReady(false);
                session.getOtherTradePlayer(player).setReady(false);
            } else {
                me.setReady(true);
                module.sendMessage(player, "trade-ready");
                if (session.getOtherTradePlayer(player).isReady()) {
                     completeTrade(session);
                }
            }
            updateInventories(session);
        }
    }
    
    private void updateMyItems(TradePlayer me, Inventory inv) {
        me.getItems().clear();
        for (int slot : module.getOwnSlots()) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                me.getItems().add(item.clone());
            }
        }
    }
    
    private void updateInventories(TradeSession session) {
        Player p1 = session.getTradePlayer1().getPlayer();
        Player p2 = session.getTradePlayer2().getPlayer();
        
        if (p1.getOpenInventory().getTopInventory().getHolder() instanceof TradeInventoryHolder h1) {
            h1.updateItems();
            h1.updateButtons();
        }
        
        if (p2.getOpenInventory().getTopInventory().getHolder() instanceof TradeInventoryHolder h2) {
            h2.updateItems();
            h2.updateButtons();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof TradeInventoryHolder holder)) return;
        
        TradeSession session = holder.getSession();
        if (session.getState() == TradeState.COMPLETED) return;
        
        Player player = (Player) event.getPlayer();
        TradePlayer me = session.getTradePlayer(player);
        TradePlayer other = session.getOtherTradePlayer(player);
        
        returnItems(me);
        returnItems(other);
        
        module.getTradeManager().removeTrade(session);
        
        if (other.getPlayer().isOnline()) {
            other.getPlayer().closeInventory();
            module.sendMessage(other.getPlayer(), "trade-cancelled");
        }
        
        module.sendMessage(player, "trade-cancelled");
        
        new TradeCancelEvent(me.getPlayer(), other.getPlayer(), player).callEvent();
    }
    
    private void returnItems(TradePlayer tradePlayer) {
        Player player = tradePlayer.getPlayer();
        for (ItemStack item : tradePlayer.getItems()) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(item);
            } else {
                player.getWorld().dropItem(player.getLocation(), item);
                module.sendMessage(player, "inventory-full");
            }
        }
    }
    
    private void completeTrade(TradeSession session) {
        session.setState(TradeState.COMPLETED);
        
        TradePlayer p1 = session.getTradePlayer1();
        TradePlayer p2 = session.getTradePlayer2();
        
        for (ItemStack item : p2.getItems()) {
            if (p1.getPlayer().getInventory().firstEmpty() != -1) {
                p1.getPlayer().getInventory().addItem(item);
            } else {
                p1.getPlayer().getWorld().dropItem(p1.getPlayer().getLocation(), item);
                module.sendMessage(p1.getPlayer(), "inventory-full");
            }
        }
        
        for (ItemStack item : p1.getItems()) {
            if (p2.getPlayer().getInventory().firstEmpty() != -1) {
                p2.getPlayer().getInventory().addItem(item);
            } else {
                p2.getPlayer().getWorld().dropItem(p2.getPlayer().getLocation(), item);
                module.sendMessage(p2.getPlayer(), "inventory-full");
            }
        }
        
        module.sendMessage(p1.getPlayer(), "trade-completed");
        module.sendMessage(p2.getPlayer(), "trade-completed");
        
        module.getTradeManager().removeTrade(session);
        
        p1.getPlayer().closeInventory();
        p2.getPlayer().closeInventory();
        
        new TradeCompleteEvent(p1.getPlayer(), p2.getPlayer()).callEvent();
    }
}

