package fr.maxlego08.essentials.module.modules.trade.model;

import fr.maxlego08.essentials.module.modules.trade.enums.TradeState;
import org.bukkit.entity.Player;

public class TradeSession {

    private final TradePlayer player1;
    private final TradePlayer player2;
    private TradeState state = TradeState.WAITING;
    private int countdownTask = -1;

    public TradeSession(Player p1, Player p2) {
        this.player1 = new TradePlayer(p1);
        this.player2 = new TradePlayer(p2);
    }

    public TradePlayer getTradePlayer1() {
        return player1;
    }

    public TradePlayer getTradePlayer2() {
        return player2;
    }
    
    public TradePlayer getTradePlayer(Player player) {
        if (player1.getPlayer().equals(player)) return player1;
        if (player2.getPlayer().equals(player)) return player2;
        return null;
    }
    
    public TradePlayer getOtherTradePlayer(Player player) {
        if (player1.getPlayer().equals(player)) return player2;
        if (player2.getPlayer().equals(player)) return player1;
        return null;
    }

    public TradeState getState() {
        return state;
    }

    public void setState(TradeState state) {
        this.state = state;
    }
    
    public void setCountdownTask(int taskId) {
        this.countdownTask = taskId;
    }
    
    public int getCountdownTask() {
        return countdownTask;
    }
}

