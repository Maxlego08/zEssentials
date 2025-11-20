package fr.maxlego08.essentials.module.modules.trade.model;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TradePlayer {

    private final Player player;
    private final List<ItemStack> items = new ArrayList<>();
    private double money = 0;
    private boolean isReady = false;
    private boolean hasConfirmed = false;

    public TradePlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
    
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
    
    public void addMoney(double amount) {
        this.money += amount;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
    
    public boolean hasConfirmed() {
        return hasConfirmed;
    }
    
    public void setConfirmed(boolean confirmed) {
        this.hasConfirmed = confirmed;
    }
}

