package fr.maxlego08.essentials.module.modules.trade.listeners;

import fr.maxlego08.essentials.module.modules.trade.TradeModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TradeInventoryListener implements Listener {

    private final TradeModule tradeModule;

    public TradeInventoryListener(TradeModule tradeModule) {
        this.tradeModule = tradeModule;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Trade inventory click handling
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Trade inventory close handling
    }
}


