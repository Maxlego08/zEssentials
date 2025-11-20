package fr.maxlego08.essentials.api.event.events.trade;

import fr.maxlego08.essentials.api.event.CancellableEssentialsEvent;
import org.bukkit.entity.Player;

public class TradeStartEvent extends CancellableEssentialsEvent {

    private final Player player1;
    private final Player player2;

    public TradeStartEvent(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}

