package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.Baltop;
import fr.maxlego08.essentials.api.economy.Economy;
import fr.maxlego08.essentials.api.economy.UserBaltop;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ZBaltop implements Baltop {

    private final Economy economy;
    private final List<UserBaltop> baltops;
    private final Map<UUID, Long> userPositions;
    private final long createdAt;

    public ZBaltop(Economy economy, List<UserBaltop> baltops, Map<UUID, Long> userPositions) {
        this.economy = economy;
        this.baltops = baltops;
        this.createdAt = System.currentTimeMillis();
        this.userPositions = userPositions;
    }

    @Override
    public Economy getEconomy() {
        return this.economy;
    }

    @Override
    public Optional<UserBaltop> getPosition(int position) {
        try {
            return Optional.ofNullable(this.baltops.get(position));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @Override
    public long getUserPosition(UUID uuid) {
        return this.userPositions.getOrDefault(uuid, -1L);
    }

    @Override
    public long getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "ZBaltop{" + "economy=" + economy.getName() + ", baltops=" + baltops + ", userPositions=" + userPositions + ", createdAt=" + createdAt + '}';
    }

    @Override
    public List<UserBaltop> getUsers() {
        return this.baltops;
    }
}
