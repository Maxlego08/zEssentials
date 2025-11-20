package fr.maxlego08.essentials.api.event.events.trade;

import fr.maxlego08.essentials.api.event.EssentialsEvent;
import org.bukkit.entity.Player;

public class TradeCancelEvent extends EssentialsEvent {

    private final Player player1;
    private final Player player2;
    private final Player canceller;

    public TradeCancelEvent(Player player1, Player player2, Player canceller) {
        this.player1 = player1;
        this.player2 = player2;
        this.canceller = canceller;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCanceller() {
        return canceller;
    }
}

