package fr.maxlego08.essentials.economy;

import fr.maxlego08.essentials.api.economy.UserBaltop;

import java.math.BigDecimal;
import java.util.UUID;

public class ZUserBaltop implements UserBaltop {

    private final UUID uuid;
    private final String name;
    private final BigDecimal bigDecimal;

    public ZUserBaltop(UUID uuid, String name, BigDecimal bigDecimal) {
        this.uuid = uuid;
        this.name = name;
        this.bigDecimal = bigDecimal;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public BigDecimal getAmount() {
        return this.bigDecimal;
    }

    @Override
    public String toString() {
        return "ZUserBaltop{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", bigDecimal=" + bigDecimal +
                '}';
    }
}
