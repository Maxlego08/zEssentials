package fr.maxlego08.essentials.module.modules.trade;

import fr.maxlego08.essentials.ZEssentialsPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TradeManager {

    private final ZEssentialsPlugin plugin;
    private final TradeModule tradeModule;
    private final Map<UUID, TradeRequest> activeTrades = new HashMap<>();

    public TradeManager(ZEssentialsPlugin plugin, TradeModule tradeModule) {
        this.plugin = plugin;
        this.tradeModule = tradeModule;
    }

    public void cancelAllTrades() {
        for (TradeRequest request : activeTrades.values()) {
            if (request != null) {
                Player player1 = request.getPlayer1();
                Player player2 = request.getPlayer2();
                if (player1 != null && player1.isOnline()) {
                    player1.closeInventory();
                }
                if (player2 != null && player2.isOnline()) {
                    player2.closeInventory();
                }
            }
        }
        activeTrades.clear();
    }

    public Map<UUID, TradeRequest> getActiveTrades() {
        return activeTrades;
    }
}


