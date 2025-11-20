package fr.maxlego08.essentials.module.modules.trade;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import fr.maxlego08.essentials.api.event.events.trade.TradeStartEvent;
import fr.maxlego08.essentials.module.modules.trade.model.TradeSession;
import fr.maxlego08.essentials.module.modules.trade.inventory.TradeInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class TradeManager {

    private final ZEssentialsPlugin plugin;
    private final TradeModule module;
    private final Map<UUID, UUID> requests = new HashMap<>();
    private final Map<UUID, TradeSession> activeTrades = new HashMap<>();

    public TradeManager(ZEssentialsPlugin plugin, TradeModule module) {
        this.plugin = plugin;
        this.module = module;
    }

    public void sendRequest(Player sender, Player target) {
        if (sender.equals(target)) {
             module.sendMessage(sender, "yourself");
             return;
        }
        
        if (activeTrades.containsKey(sender.getUniqueId())) {
            module.sendMessage(sender, "already-trading");
            return;
        }
        
        if (activeTrades.containsKey(target.getUniqueId())) {
            module.sendMessage(sender, "target-already-trading", "%player%", target.getName());
            return;
        }
        
        requests.put(target.getUniqueId(), sender.getUniqueId());
        
        module.sendMessage(sender, "request-sent", "%player%", target.getName());
        module.sendMessage(target, "request-received", "%player%", sender.getName());

        plugin.getScheduler().runLater(() -> {
            if (requests.get(target.getUniqueId()) != null && requests.get(target.getUniqueId()).equals(sender.getUniqueId())) {
                requests.remove(target.getUniqueId());
            }
        }, module.getRequestTimeout() * 20);
    }

    public void acceptRequest(Player sender, Player target) {
        if (!requests.containsKey(sender.getUniqueId()) || !requests.get(sender.getUniqueId()).equals(target.getUniqueId())) {
            module.sendMessage(sender, "no-request", "%player%", target.getName());
            return;
        }

        if (activeTrades.containsKey(target.getUniqueId())) {
            module.sendMessage(sender, "target-already-trading", "%player%", target.getName());
            return;
        }
        
        if (sender.getLocation().distance(target.getLocation()) > module.getMaxDistance()) {
             module.sendMessage(sender, "player-too-far");
             return;
        }

        requests.remove(sender.getUniqueId());
        module.sendMessage(target, "request-accepted");
        startTrade(sender, target);
    }
    
    public void denyRequest(Player sender, Player target) {
        if (requests.containsKey(sender.getUniqueId()) && requests.get(sender.getUniqueId()).equals(target.getUniqueId())) {
            requests.remove(sender.getUniqueId());
            module.sendMessage(sender, "request-denied");
            module.sendMessage(target, "request-denied");
        } else {
            module.sendMessage(sender, "no-request", "%player%", target.getName());
        }
    }

    public void startTrade(Player p1, Player p2) {
        TradeStartEvent event = new TradeStartEvent(p1, p2);
        event.callEvent();
        if (event.isCancelled()) return;

        TradeSession session = new TradeSession(p1, p2);
        activeTrades.put(p1.getUniqueId(), session);
        activeTrades.put(p2.getUniqueId(), session);
        
        new TradeInventoryHolder(p1, session, module).open();
        new TradeInventoryHolder(p2, session, module).open();
    }

    public void cancelAllTrades() {
        for (TradeSession session : new ArrayList<>(activeTrades.values())) {
            // Logic to close inventory and return items will be handled by listener or manually here if needed
        }
        activeTrades.clear();
        requests.clear();
    }
    
    public void removeTrade(TradeSession session) {
        activeTrades.remove(session.getTradePlayer1().getPlayer().getUniqueId());
        activeTrades.remove(session.getTradePlayer2().getPlayer().getUniqueId());
    }
    
    public TradeSession getTradeSession(Player player) {
        return activeTrades.get(player.getUniqueId());
    }
    
    public Map<UUID, UUID> getRequests() {
        return requests;
    }
    
    public Map<UUID, TradeSession> getActiveTrades() {
        return activeTrades;
    }
}

