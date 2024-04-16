package fr.maxlego08.essentials.api.utils;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCache {
    private final Set<String> players = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void addPlayer(String playerName) {
        players.add(playerName);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players.clear();
        this.players.addAll(players);
    }
}
